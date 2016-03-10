package no.nsd.qddt.domain;

import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.version.Version;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * Creates and updates entities based on global rules
 * of implementations of {@link AbstractEntity} and {@link AbstractEntityAudit}.
 *
 * Be aware that this class will cause {@link NullPointerException} if BeforeSecurityContext is not set in tests.
 *
 * @author Dag Østgulen Heradstveit
 */
@Configurable
public class EntityCreatedModifiedDateAuditEventConfiguration {

    /**
     * Run before persisting a new entity.
     * @param entity target for persistence
     */
    @PrePersist
    public void create(AbstractEntity entity) {
        try {
            LocalDateTime now = LocalDateTime.now();
            entity.setUpdated(now);
            User user = SecurityContext.getUserDetails().getUser();
            entity.setCreatedBy(user);
            if (entity instanceof AbstractEntityAudit) {
                ((AbstractEntityAudit) entity).setAgency(user.getAgency());
                ((AbstractEntityAudit) entity).setChangeKind(AbstractEntityAudit.ChangeKind.CREATED);
                Version version = new Version();
                version.incMinor();
                ((AbstractEntityAudit) entity).setVersion(version);
            }
//            if (entity instanceof Authorable){
//                if (!((Authorable) entity).getAuthors().contains(user)) {
//                    ((Authorable) entity).addAuthor(user);
//                }
//            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Runs before updating an existing entity.
     * @param entity target for persistence
     */
    @PreUpdate
    public void update(AbstractEntity entity) {
        try {
            entity.setUpdated(LocalDateTime.now());
            User user = SecurityContext.getUserDetails().getUser();
            entity.setCreatedBy(user);

            if (entity instanceof AbstractEntityAudit) {
                Version ver = ((AbstractEntityAudit) entity).getVersion();
                AbstractEntityAudit.ChangeKind change = ((AbstractEntityAudit) entity).getChangeKind();

                if (change == AbstractEntityAudit.ChangeKind.CREATED) {
                    change = AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT;
                    ((AbstractEntityAudit) entity).setChangeKind(change);
                }
                switch (change) {
                    case NEW_COPY_OF:
                        ver = new Version();
                        ver.incMajor();
                        System.out.println("PREUPDATE -> NEW_COPY_OF");
                        break;
                    case NEW_MAJOR:
                        ver.incMajor();
                        System.out.println("PREUPDATE -> NEW_MAJOR ");
                        break;
                    case NEW_MINOR:
                    case TYPO:
                        ver.incMinor();
                        System.out.println("PREUPDATE -> NEW_MINOR");
                        break;
                    default:
                        break;
                }
                ((AbstractEntityAudit) entity).setVersion(ver);
            }

//            if (entity instanceof Authorable){
//                User author = SecurityContext.getUserDetails().getUser();
//                if (!((Authorable) entity).getAuthors().contains(author)) {
//                    ((Authorable) entity).addAuthor(author);
//                }
//            }

        } catch (Exception e) {
            System.out.println("ERROR -> " + e.getClass().toString() + " - " +  e.getMessage());
            System.out.println(entity);
        }
    }


}