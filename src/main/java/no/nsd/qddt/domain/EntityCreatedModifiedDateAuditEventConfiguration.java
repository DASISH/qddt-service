package no.nsd.qddt.domain;

import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.version.SemVer;
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
            entity.setCreated(now);
            User user = SecurityContext.getUserDetails().getUser();
            entity.setCreatedBy(user);
            if (entity instanceof AbstractEntityAudit) {
                ((AbstractEntityAudit) entity).setAgency(user.getAgency());
                ((AbstractEntityAudit) entity).setChangeKind(AbstractEntityAudit.ChangeKind.CREATED);
                ((AbstractEntityAudit) entity).setVersion("0.0.1");
            }
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

            if (entity instanceof AbstractEntityAudit) {
                SemVer ver = ((AbstractEntityAudit) entity).getSemVer();
                AbstractEntityAudit.ChangeKind change = ((AbstractEntityAudit) entity).getChangeKind();

                if (change == AbstractEntityAudit.ChangeKind.CREATED) {
                    change = AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT;
                }
                switch (change) {
                    case NEW_COPY_OF:
                        ver = new SemVer();
                        ver.incMajor();
                        System.out.println("PREUPDATE -> NEW_COPY_OF");
                        break;
                    case NEW_MAJOR:
                        ver.incMajor();
                        System.out.println("PREUPDATE -> NEW_MAJOR ");
                        break;
                    case NEW_MINOR:
                        ver.setMinor();
                        System.out.println("PREUPDATE -> NEW_MINOR");
                        break;
                    default:
                        ver.incPatch();
                        break;
                }
                ((AbstractEntityAudit) entity).setChangeKind(change);
                ((AbstractEntityAudit) entity).setVersion(ver.toString());
            }
        } catch (Exception e) {
            System.out.println("ERROR -> " + e.getClass().toString() + " - " +  e.getMessage());
            System.out.println(entity);
        }
    }


}