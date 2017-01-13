package no.nsd.qddt.domain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.user.User;
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
//            System.out.println("create");
            entity.setModified(LocalDateTime.now());
            entity.setModifiedBy(SecurityContext.getUserDetails().getUser());

            if (entity instanceof Category) {
                entity = fixAndValidateCategoryType((Category)entity);
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
//        System.out.println("@PreUpdate " + entity.getClass().getName());
        try {
            entity.setModified(LocalDateTime.now());
            entity.setModifiedBy(SecurityContext.getUserDetails().getUser());

            if (entity instanceof SurveyProgram){
                checkAuthor((SurveyProgram)entity);
            }
            if (entity instanceof Study){
                checkAuthor((Study)entity);
            }
            if (entity instanceof TopicGroup){
                checkAuthor((TopicGroup)entity);
            }
            if (entity instanceof Concept) {
                checkAddedQuestions((Concept) entity);
            }


        } catch (Exception e) {
            System.out.println("ERROR -> " + e.getClass().toString() + " - " +  e.getMessage());
            System.out.println(entity);
        }
    }


    private boolean isBasedOnCopy(AbstractEntityAudit rootEntity){
        return (rootEntity.getId() == null &&
                rootEntity.getBasedOnObject() != null &&
                rootEntity.getChangeKind() == AbstractEntityAudit.ChangeKind.BASED_ON);
    }

    private AbstractEntityAudit makeBasedOnCopy(AbstractEntityAudit rootEntity){
        rootEntity.setBasedOnObject(rootEntity.getId());
        rootEntity.setId(null);
        rootEntity.setChangeKind(AbstractEntityAudit.ChangeKind.BASED_ON);
        return rootEntity;
    }

    private boolean isAnOwner(AbstractEntityAudit entity, User user){
        return entity.getAgency().equals(user.getAgency());
    }

    private Category fixAndValidateCategoryType(Category input){
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


    private SurveyProgram checkAuthor(SurveyProgram entity){
        entity.getAuthors().forEach(a->a.addSurvey(entity));
        return entity;
    }

    private Study checkAuthor(Study entity){
        entity.getAuthors().forEach(a->a.addStudy(entity));
        return entity;
    }

    private TopicGroup checkAuthor(TopicGroup entity){
        entity.getAuthors().forEach(a->a.addTopic(entity));
        return entity;
    }

    private Concept checkAddedQuestions(Concept entity) {
        entity.getQuestionItems()
                .forEach(qi -> {
                    if (!qi.getConcepts().contains(entity)) {
                        qi.getConcepts().add(entity);
                        entity.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_HIERARCY_RELATION);
                        entity.setChangeComment("added question, " + qi.getName());
                    }
                });
        return entity;
    }



}