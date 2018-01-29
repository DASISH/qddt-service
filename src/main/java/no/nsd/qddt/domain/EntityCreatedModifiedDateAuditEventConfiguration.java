package no.nsd.qddt.domain;

import no.nsd.qddt.utils.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    /**
     * Run before persisting a new entity.
     * @param entity target for persistence
     */
    @SuppressWarnings("UnusedAssignment")
    @PrePersist
    @PreUpdate
    public void createOrUpdate(AbstractEntity entity) {
        try {
            entity.setModified(LocalDateTime.now());
            entity.setModifiedBy(SecurityContext.getUserDetails().getUser());

            if (entity instanceof AbstractEntityAudit )
                LOG.debug("Entity EventConfiguration CreateOrUpdate "+ entity.getClass().getSimpleName() + " - " +
                    ((AbstractEntityAudit)entity).getName());
            else
                LOG.debug("Entity EventConfiguration CreateOrUpdate "+ entity.getClass().getSimpleName() + " - " +
                        entity.toString());


        } catch (Exception e){
            LOG.error("Entity EventConfiguration", e);
        }
    }

}