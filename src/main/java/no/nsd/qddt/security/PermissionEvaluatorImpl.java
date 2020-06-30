package no.nsd.qddt.security;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.user.QDDTUserDetails;
import no.nsd.qddt.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Stig Norland
 */
@Component
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    private enum PermissionType { OWNER, USER, AGENCY }
    protected final Logger LOG = LoggerFactory.getLogger(PermissionEvaluatorImpl.class);

    @Override
    public boolean hasPermission( Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) ||
            !(permission instanceof String) ||
            !(targetDomainObject instanceof AbstractEntity) ){
            LOG.info( "Prereq for hasPermission not fulfilled" );
            return false;
        }

        return this.hasPrivilege( (QDDTUserDetails)auth.getPrincipal(), (AbstractEntity)targetDomainObject, ((String) permission).toUpperCase());
    }


    @Override
    public boolean hasPermission( Authentication auth, Serializable targetId, String targetType, Object permission)  {
        LOG.error( "hasPermission (4 args) not implemented" );
        return false;
    }


    private boolean hasPrivilege(QDDTUserDetails details, AbstractEntity entity, String permission){
//        LOG.info( details.getUsername() + ": " + permission + ": " + toJson(entity)  );
        assert entity != null;
        if ( entity.getId() == null || entity.getModifiedBy() == null)
            return true;
        switch (PermissionType.valueOf( permission )) {
            case OWNER:
                return isOwner( details.getUser(), entity );
            case USER:
                return isUser( details.getUser(), entity );
            case AGENCY:

                return isMemberOfAgency( details.getUser().getAgency(), (AbstractEntityAudit )entity );
            default:
                LOG.info( "hasPrivilege default: fail: " + permission );
                return false;
        }
    }

    private boolean isOwner(User user, AbstractEntity entity) {
        assert entity.getModifiedBy() != null;
        return user.getId().equals( entity.getModifiedBy().getId() );
    }

    // entity is a User entity
    private boolean isUser(User user, AbstractEntity entity) {
        assert entity.getId() != null;
        return ( user.getId().equals( entity.getId() ));
    }

    private boolean isMemberOfAgency(Agency agency, AbstractEntityAudit entity) {
        assert entity.getAgency() != null;
        if (agency.getId().equals( entity.getAgency().getId()))
            return true;

        LOG.info( agency.getName() + " != " + entity.getAgency().getName() );
        return false;
    }

    public String toJson(AbstractEntity entity) {
        return "{" +
            "\"id\":" + (entity.getId() == null ? "null" : "\"" + entity.getId() +"\"" ) + ", " +
            "\"modified\":" + (entity.getModified() == null ? "null" : "\"" + entity.getModified()+ "\"" ) + " , " +
            "\"classKind\":" + entity.getClass().getSimpleName() + "\"" +
            "}";
    }
}
