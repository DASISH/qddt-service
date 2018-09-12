package no.nsd.qddt.security;

import javassist.NotFoundException;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.user.QDDTUserDetails;
import no.nsd.qddt.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    private enum PermissionType { OWNER, USER, AGENCY }
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean hasPermission( Authentication auth, Object targetDomainObject, Object permission) {
        LOG.info("hasPermission implemented");
        if ((auth == null) || (targetDomainObject == null) ||
            !(permission instanceof String) ||
            !(targetDomainObject instanceof AbstractEntityAudit) ){
            return false;
        }
        try {
            return hasPrivilege(((QDDTUserDetails)auth.getDetails()), (AbstractEntityAudit)targetDomainObject, ((String) permission).toUpperCase());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasPrivilege(QDDTUserDetails details, AbstractEntityAudit entity, String permission) throws NotFoundException {
        switch (PermissionType.valueOf( permission )) {
            case OWNER:
                return isOwner( details.getUser(), entity );
            case USER:
                return isUser( details.getUser(), entity );
            case AGENCY:
                return isAgency( details.getUser().getAgency(), entity );
            default:
                throw  new NotFoundException( "Valid permissions: " + permissions2str());
        }
    }

    @Override
    public boolean hasPermission( Authentication auth, Serializable targetId, String targetType, Object permission)  {
        LOG.info("hasPermission NOT implemented");
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return  false;
    }

    private boolean isOwner(User user, AbstractEntityAudit entity) {
        System.out.println("hasPermission isOwner?");
        return  ( user.getId().equals( entity.getModifiedBy().getId() ));
    }

    private boolean isUser(User user, AbstractEntityAudit entity) {
        return  false;
    }

    private boolean isAgency( Agency agency, AbstractEntityAudit entity) {
        return (agency.equals( entity.getAgency()));
    }

    private String permissions2str() {
        return Arrays.stream( PermissionType.values() )
            .map( f -> f.name() )
            .collect( Collectors.joining(","));
    }
}