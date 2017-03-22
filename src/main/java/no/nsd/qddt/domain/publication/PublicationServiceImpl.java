package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.study.audit.StudyAuditService;
import no.nsd.qddt.domain.surveyprogram.audit.SurveyProgramAuditService;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("selectableService")
public class PublicationServiceImpl implements PublicationService {

    private PublicationRepository repository;
    private ConceptAuditService conceptService;
    private ControlConstructAuditService controlConstructService;
    private InstrumentAuditService instrumentService;
    private QuestionItemAuditService questionItemService;
    private StudyAuditService studyService;
    private SurveyProgramAuditService surveyProgramService;
    private TopicGroupAuditService topicGroupService;

    @Autowired
    public PublicationServiceImpl(PublicationRepository repository,
                                  ConceptAuditService conceptService,
                                  ControlConstructAuditService controlConstructService,
                                  InstrumentAuditService instrumentService,
                                  QuestionItemAuditService questionItemService,
                                  StudyAuditService studyService,
                                  SurveyProgramAuditService surveyProgramService,
                                  TopicGroupAuditService topicGroupService)
        {
        this.repository = repository;
        this.conceptService = conceptService;
        this.controlConstructService = controlConstructService;
        this.instrumentService = instrumentService;
        this.questionItemService = questionItemService;
        this.studyService = studyService;
        this.surveyProgramService = surveyProgramService;
        this.topicGroupService = topicGroupService;
    }

    @Override
    public long count() {
        return repository.count();
    }


    @Override
    public boolean exists(UUID uuid) {
        return repository.exists(uuid);
    }


    @Override
//    @Transactional(readOnly = true)
    public Publication findOne(UUID uuid) {
        return fillElements(repository.findOne(uuid));
    }


    @Override
    @Transactional()
    public Publication save(Publication instance) {
        return repository.save(instance);
    }


    @Override
    @Transactional()
    public List<Publication> save(List<Publication> instances) {
        return repository.save(instances);
    }


    @Override
    @Transactional()
    public void delete(UUID uuid) {
        repository.delete(uuid);
    }


    @Override
    @Transactional()
    public void delete(List<Publication> instances) {
        repository.delete(instances);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Publication> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable);
//                .map(this::fillElements);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Publication> findByNameOrPurposeAndStatus(String name, String purpose, String status, Pageable pageable) {
        return repository.findByStatusLikeAndNameIgnoreCaseLikeOrPurposeIgnoreCaseLike(status,name,purpose,pageable);
//        .map(this::fillElements);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationElement getDetail(PublicationElement publicationElement) {
        return fill(publicationElement);
    }


    private Publication fillElements(Publication publication){
        publication.getPublicationElements().forEach(this::fill);
        return publication;
    }

//    private Publication fillElementsSimple(Publication publication){
//        publication.getPublicationElements().forEach(this::fillSimple);
//        return publication;
//    }

    private BaseServiceAudit getService(ElementKind elementKind){
         switch (elementKind) {
            case CONCEPT:
                return conceptService;
            case CONTROL_CONSTRUCT:
            case QUESTION_CONSTRUCT:
            case STATEMENT_CONSTRUCT:
            case SEQUENCE_CONSTRUCT:
                return controlConstructService;
            case INSTRUMENT:
                return instrumentService;
            case QUESTION_ITEM:
                return questionItemService;
            case STUDY:
                return studyService;
            case SURVEY_PROGRAM:
                return surveyProgramService;
            case TOPIC_GROUP:
                return topicGroupService;
        }
        return null;
    }

    private PublicationElement fill(PublicationElement element) {
        try {
            BaseServiceAudit service = getService(element.getElementEnum());
            element.setElement(service.findRevision(
                    element.getId(),
                    element.getRevisionNumber())
                    .getEntity());

        } catch (RevisionDoesNotExistException e) {
            element.setElement(conceptService.findLastChange(element.getId()));
        } catch (JpaSystemException se) {
            System.out.println(se.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        System.out.println("fill  -> " + element);
        return element;
    }

//    private PublicationElement fillSimple(PublicationElement element){
//        BaseServiceAudit service = getService(element.getElementEnum());
//        try{
//            element.setElement((AbstractEntityAudit)service.findRevision(
//                    element.getId(),
//                    element.getRevisionNumber())
//                    .getEntity());
//
//        } catch (RevisionDoesNotExistException e){
//            element.setElement((AbstractEntityAudit) service.findLastChange(element.getId()).getEntity());
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return element;
//    }


}
