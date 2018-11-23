package no.nsd.qddt.security;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.comment.Comment;
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
            !(targetDomainObject instanceof AbstractEntity)  ){
            return false;
        }

        if (targetDomainObject instanceof Comment) {
            User user = ((QDDTUserDetails) auth.getPrincipal()).getUser();
            Comment comment = (Comment) targetDomainObject;

            return  comment.getModifiedBy() == null ||  (user.getId().equals(  comment.getModifiedBy().getId()));

        } else if (targetDomainObject instanceof AbstractEntityAudit) {
            return hasPrivilege( (QDDTUserDetails) auth.getPrincipal(), (AbstractEntityAudit) targetDomainObject, ((String) permission).toUpperCase() );
        }
        return  false;
    }


    @Override
    public boolean hasPermission( Authentication auth, Serializable targetId, String targetType, Object permission)  {
        LOG.error( "hasPermission (4 args) not implemented" );
        return false;
    }


    private boolean hasPrivilege(QDDTUserDetails details, AbstractEntityAudit entity, String permission){
        LOG.info( details.getUsername() + " - " + entity.getName() + " - " + permission );
        assert entity != null;
        if ( entity.getId() == null || entity.getModifiedBy() == null)
            return true;
        switch (PermissionType.valueOf( permission )) {
            case OWNER:
                return isOwner( details.getUser(), entity );
            case USER:
                return isUser( details.getUser(), entity );
            case AGENCY:
                return isMemberOfAgency( details.getUser().getAgency(), entity );
            default:
                return false;
        }
    }

    private boolean isOwner(User user, AbstractEntityAudit entity) {
        return  ( entity.getModifiedBy() == null || user.getId().equals( entity.getModifiedBy().getId() ));
    }

    private boolean isUser(User user, AbstractEntityAudit entity) {
        return ( user.getId().equals( entity.getId() ));
    }

    private boolean isMemberOfAgency(Agency agency, AbstractEntityAudit entity) {
        if (entity.getAgency()  == null || entity.getModifiedBy() == null ) return  true;

        boolean isMember = agency.getId().equals( entity.getAgency().getId());
        LOG.info( String.valueOf( isMember ) );
        return (isMember);
    }

}