package no.nsd.qddt.domain;

import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * Creates and updates entities based on global rules
 * of implementations of {@link AbstractEntity} and {@link AbstractEntityAudit}
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
        if (entity instanceof AbstractEntityAudit) {
            ((AbstractEntityAudit)entity).setChangeReason(AbstractEntityAudit.ChangeKind.CREATED);
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
            if (((AbstractEntityAudit)entity).getChangeReason() == AbstractEntityAudit.ChangeKind.CREATED)
                ((AbstractEntityAudit)entity).setChangeReason(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT);
        }
    }
}