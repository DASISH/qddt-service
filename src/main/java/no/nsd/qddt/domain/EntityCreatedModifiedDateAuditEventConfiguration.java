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
    @SuppressWarnings("UnusedAssignment")
    @PrePersist
    public void create(AbstractEntity entity) {
        try {
            entity.setModified(LocalDateTime.now());
            entity.setModifiedBy(SecurityContext.getUserDetails().getUser());

            if (entity instanceof Category) {
                //noinspection UnusedAssignment
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
        try {
            entity.setModified(LocalDateTime.now());
            entity.setModifiedBy(SecurityContext.getUserDetails().getUser());

            if (entity instanceof SurveyProgram){
                checkSurvey((SurveyProgram)entity);
            }
            if (entity instanceof Study){
                checkStudy((Study)entity);
            }
            if (entity instanceof TopicGroup){
                checkTopic((TopicGroup)entity);
            }
            if (entity instanceof Concept) {
                checkConcept((Concept) entity);
            }


        } catch (Exception e) {
            System.out.println("ERROR -> " + e.getClass().toString() + " - " +  e.getMessage());
            System.out.println(entity);
        }
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


    /*
    Authors bidirectional relationship needs to be updated manually... FIX ME...
     */
    private SurveyProgram checkSurvey(SurveyProgram entity){
        entity.getAuthors().forEach(a->a.addSurvey(entity));
        return entity;
    }

    /*
    Authors bidirectional relationship needs to be updated manually... FIX ME...
     */
    private Study checkStudy(Study entity){
        entity.getAuthors().forEach(a->a.addStudy(entity));
        return entity;
    }

    /*
    Authors bidirectional relationship needs to be updated manually... FIX ME...
     */
    private TopicGroup checkTopic(TopicGroup entity){
        entity.getAuthors().forEach(a->a.addTopic(entity));
        return entity;
    }

    /*
    Code to set status UPDATED_HIERARCHY_RELATION when adding questionItem to Concept...

     */
    private Concept checkConcept(Concept concept) {
//        concept.getConceptQuestionItems().stream().forEach(cqi->{
//            if (!cqi.getQuestionItem().getConceptQuestionItems().stream().anyMatch(cqi2 -> cqi2.getConcept().equals(concept)))
//                if(cqi.getUpdated() == null){ //this is a unsaved entity, lets try to add concept to QI...
//                    System.out.println("Concept <-> QuestionItem trying to sync");
//                    cqi.setQuestionItemRevision(null);
//                    cqi.getQuestionItem().addConcept(concept);
//                }
//                else
//                    System.out.println("Concept <-> QuestionItem not in sync, but most likely due to link to old version");
//                });
        return concept;
    }



}