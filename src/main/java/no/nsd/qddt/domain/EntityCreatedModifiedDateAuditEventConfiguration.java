package no.nsd.qddt.domain;

import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * Creates and updates
 *
 * @author Dag Østgulen Heradstveit
 */
@Configurable
public class EntityCreatedModifiedDateAuditEventConfiguration {

    @PrePersist
    public void create(AbstractEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        if(entity.getCreated() != null)
            entity.setUpdated(now);
        else
            entity.setCreated(now);
    }

    @PreUpdate
    public void update(AbstractEntity entity) {
        entity.setUpdated(LocalDateTime.now());
    }
}