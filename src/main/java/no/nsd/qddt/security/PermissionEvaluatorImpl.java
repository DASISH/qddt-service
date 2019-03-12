package no.nsd.qddt.security;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.IArchived;
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
            !(permission instanceof String)  ){
            return false;
        }

        User user = ((QDDTUserDetails) auth.getPrincipal()).getUser();
        String PERMISSION = ((String) permission).toUpperCase();

        if (targetDomainObject instanceof Iterable) {
            for (AbstractEntity abstractEntity : ((Iterable<AbstractEntity>) targetDomainObject)) {
                if (!hasPrivilege( user, abstractEntity, PERMISSION ))
                    return false;
            }
            return true;
        } else
            return hasPrivilege( user, (AbstractEntity) targetDomainObject, PERMISSION );
    }


    @Override
    public boolean hasPermission( Authentication auth, Serializable targetId, String targetType, Object permission)  {
        LOG.error( "hasPermission (4 args) not implemented" );
        return false;
    }


    private boolean hasPrivilege(User user, AbstractEntity entity, String permission){
        LOG.debug( user.getUsername() + " - " + entity.getClass().getName() + " - " + permission );
        assert entity != null;
        if ( entity.getId() == null || entity.getModifiedBy() == null)
            return true;

        if (isArchived( entity )) return false;

        switch (PermissionType.valueOf( permission )) {
            case OWNER:
                return isOwner( user, entity );
            case USER:
                return isUser( user, entity );
            case AGENCY:
                return isMemberOfAgency( user.getAgency(), (AbstractEntityAudit)entity );
            default:
                return false;
        }
    }

    private boolean isArchived(AbstractEntity entity) {
        if (entity instanceof  IArchived)
            return ((IArchived) entity).isArchived();
        else
            return false;
    }

    private boolean isOwner(User user, AbstractEntity entity) {
        return  ( entity.getModifiedBy() == null || user.getId().equals( entity.getModifiedBy().getId() ));
    }

    private boolean isUser(User user, AbstractEntity entity) {
        return ( user.getId().equals( entity.getId() ));
    }

    private boolean isMemberOfAgency(Agency agency, AbstractEntityAudit entity) {
        if (entity.getAgency()  == null || entity.getModifiedBy() == null || isReferenceCopy(entity) ) return  true;

        boolean isMember = agency.getId().equals( entity.getAgency().getId());
        LOG.debug( String.valueOf( isMember ) );
        return (isMember);
    }

    private boolean isReferenceCopy(AbstractEntityAudit entity) {
        return ( entity.getChangeKind() == AbstractEntityAudit.ChangeKind.BASED_ON ||
            entity.getChangeKind() == AbstractEntityAudit.ChangeKind.NEW_COPY ||
            entity.getChangeKind() == AbstractEntityAudit.ChangeKind.TRANSLATED);
    }
}