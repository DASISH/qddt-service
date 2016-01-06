package no.nsd.qddt.domain;

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
        LocalDateTime now = LocalDateTime.now();
        entity.setCreated(now);
        entity.setCreatedBy(SecurityContext.getUserDetails().getUser());
        if (entity instanceof AbstractEntityAudit) {
            SemVer ver = new SemVer();
            ver.incPatch();
            ((AbstractEntityAudit) entity).setVersion(ver);
            ((AbstractEntityAudit)entity).setChangeKind(AbstractEntityAudit.ChangeKind.CREATED);
        }
    }

    /**
     * Runs before updating an existing entity.
     * @param entity target for persistence
     */
    @PreUpdate
    public void update(AbstractEntity entity) {
        entity.setUpdated(LocalDateTime.now());
        if(entity instanceof AbstractEntityAudit) {
            SemVer ver = ((AbstractEntityAudit) entity).getVersion();
                    ver.incPatch();
            ((AbstractEntityAudit) entity).setVersion(ver);

            if (((AbstractEntityAudit)entity).getChangeKind() == AbstractEntityAudit.ChangeKind.CREATED)
                ((AbstractEntityAudit)entity).setChangeKind(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT);
        }
    }
}