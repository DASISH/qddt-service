package no.nsd.qddt.domain;

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
    @SuppressWarnings("UnusedAssignment")
    @PrePersist
    @PreUpdate
    public void createOrUpdate(AbstractEntity entity) {
        try {
            entity.setModified(LocalDateTime.now());
            entity.setModifiedBy(SecurityContext.getUserDetails().getUser());

            System.out.println("Entity EventConfiguration CreateOrUpdate "+ entity.getClass().getSimpleName() + " - " +
                    ((AbstractEntityAudit)entity).getName());

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}