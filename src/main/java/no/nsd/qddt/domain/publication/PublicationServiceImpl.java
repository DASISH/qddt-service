package no.nsd.qddt.domain.publication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.study.audit.StudyAuditService;
import no.nsd.qddt.domain.surveyprogram.audit.SurveyProgramAuditService;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Publication findOne(UUID uuid) {
        return fillElements(repository.findOne(uuid));
    }


    @Override
    public Publication save(Publication instance) {
        return repository.save(instance);
    }


    @Override
    public List<Publication> save(List<Publication> instances) {
        return repository.save(instances);
    }


    @Override
    public void delete(UUID uuid) {
        repository.delete(uuid);
    }


    @Override
    public void delete(List<Publication> instances) {
        repository.delete(instances);
    }


    @Override
    public Page<Publication> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable);
        //.map(this::fillElements);
    }


    @Override
    public Page<Publication> findByNameOrPurposeAndStatus(String name, String purpose, String status, Pageable pageable) {
        return repository.findByStatusLikeAndNameIgnoreCaseLikeOrPurposeIgnoreCaseLike(status,name,purpose,pageable);
        // .map(this::fillElements);
    }


    private Publication fillElements(Publication publication){
        publication.getPublicationElements().forEach(this::fill);
        return publication;
    }


    private PublicationElement fill(PublicationElement element){
        try {
            switch (element.getElementEnum()) {
                case CONCEPT:
                    element.setElement(
                            conceptService.findRevision(
                                    element.getId(),
                                    element.getRevisionNumber())
                                    .getEntity());
                    break;
                case CONTROL_CONSTRUCT:
                case QUESTION_CONSTRUCT:
                case SEQUENCE_CONSTRUCT:
                case STATEMENT_CONSTRUCT:
                    element.setElement(
                            controlConstructService.findRevision(
                                    element.getId(),
                                    element.getRevisionNumber())
                                    .getEntity());
                    break;
                case INSTRUMENT:
                    element.setElement(
                            instrumentService.findRevision(
                                    element.getId(),
                                    element.getRevisionNumber())
                                    .getEntity());
                    break;
                case QUESTION_ITEM:
                    element.setElement(
                            questionItemService.findRevision(
                                    element.getId(),
                                    element.getRevisionNumber())
                                    .getEntity());
                    break;
                case STUDY:
                    element.setElement(
                            studyService.findRevision(
                                    element.getId(),
                                    element.getRevisionNumber())
                                    .getEntity());
                    break;
                case SURVEY_PROGRAM:
                    element.setElement(
                            surveyProgramService.findRevision(
                                    element.getId(),
                                    element.getRevisionNumber())
                                    .getEntity());
                    break;
                case TOPIC_GROUP:
                    element.setElement(
                            topicGroupService.findRevision(
                                    element.getId(),
                                    element.getRevisionNumber())
                                    .getEntity());
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return element;
    }

}
