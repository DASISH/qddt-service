package no.nsd.qddt.domain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.embedded.Version;
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
            entity.setModified(now);
            User user = SecurityContext.getUserDetails().getUser();
            entity.setModifiedBy(user);
            if (entity instanceof AbstractEntityAudit) {
                ((AbstractEntityAudit) entity).setAgency(user.getAgency());
                ((AbstractEntityAudit) entity).setChangeKind(AbstractEntityAudit.ChangeKind.CREATED);
                ((AbstractEntityAudit) entity).setVersion(new Version());
            }
            if (entity instanceof Category) {
                entity = FixAndValidateCategoryType((Category)entity);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private Category FixAndValidateCategoryType(Category input){
        if (input.getCategoryType() == null)
            input.setCategoryType(CategoryType.CATEGORY);
        switch (input.getCategoryType()) {
            case DATETIME:
            case TEXT:
            case NUMERIC:
            case CATEGORY:
                input.setHierarchyLevel(HierarchyLevel.ENTITY);
                break;
            case MISSING_GROUP:
            case LIST:
            case SCALE:
            case MIXED:
                input.setHierarchyLevel(HierarchyLevel.GROUP_ENTITY);
                break;
        }
        return input;
    }


    /**
     * Runs before updating an existing entity.
     * @param entity target for persistence
     */
    @PreUpdate
    public void update(AbstractEntity entity) {
        try {
            entity.setModified(LocalDateTime.now());
            User user = SecurityContext.getUserDetails().getUser();
            entity.setModifiedBy(user);

            if (entity instanceof AbstractEntityAudit) {
                Version ver = ((AbstractEntityAudit) entity).getVersion();
                AbstractEntityAudit.ChangeKind change = ((AbstractEntityAudit) entity).getChangeKind();

                if (change == AbstractEntityAudit.ChangeKind.CREATED) {
                    change = AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT;
                    ((AbstractEntityAudit) entity).setChangeKind(change);
                }
                if (change != AbstractEntityAudit.ChangeKind.MILESTONE){
                    ver.setVersionLabel("");
                }
                switch (change) {
                    case NEW_COPY_OF:
                        ver = new Version();
//                        ver.incMajor();
                        break;
                    case MILESTONE:
                    case CONCEPTUAL:
                    case EXTERNAL:
                    case OTHER:
                        ver.incMajor();
                        break;
                    case TYPO:
                        ver.incMinor();
                        break;
                    default:        //CREATED / UPDATED_PARENT / UPDATED_HIERARCY_RELATION / IN_DEVELOPMENT
                        break;
                }
                ((AbstractEntityAudit) entity).setVersion(ver);
            }

        } catch (Exception e) {
            System.out.println("ERROR -> " + e.getClass().toString() + " - " +  e.getMessage());
            System.out.println(entity);
        }
    }


}