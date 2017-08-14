package no.nsd.qddt.domain.publication;

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
import org.springframework.data.history.Revision;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Stig Norland
 */
@Service("selectableService")
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository repository;
    private final ConceptAuditService conceptService;
    private final ControlConstructAuditService controlConstructService;
    private final InstrumentAuditService instrumentService;
    private final QuestionItemAuditService questionItemService;
    private final StudyAuditService studyService;
    private final SurveyProgramAuditService surveyProgramService;
    private final TopicGroupAuditService topicGroupService;

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
        return postLoadProcessing(repository.findOne(uuid));
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


    protected Publication prePersistProcessing(Publication instance) {
        return instance;
    }


    private Publication postLoadProcessing(Publication instance) {
        instance.getPublicationElements().forEach(this::fill);
        return instance;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Publication> findAllPageable(Pageable pageable) {
        return repository.findAll(defaultSort(pageable,"name","modified"));
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Publication> findByNameOrPurposeAndStatus(String name, String purpose, String status, Pageable pageable) {
        return repository.findByStatusLikeAndNameIgnoreCaseLikeOrPurposeIgnoreCaseLike(status,name,purpose,
                defaultSort(pageable,"name","modified"));
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationElement getDetail(PublicationElement publicationElement) {
        return fill(publicationElement);
    }


    private BaseServiceAudit getService(ElementKind elementKind){
         switch (elementKind) {
            case CONCEPT:
                return conceptService;
            case CONTROL_CONSTRUCT:
            case QUESTION_CONSTRUCT:
            case STATEMENT_CONSTRUCT:
            case SEQUENCE_CONSTRUCT:
            case CONDITION_CONSTRUCT:
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
        BaseServiceAudit service = getService(element.getElementEnum());
        try {
            element.setElement(service.findRevision(
                    element.getId(),
                    element.getRevisionNumber())
                    .getEntity());

        } catch (RevisionDoesNotExistException e) {
            Revision rev = service.findLastChange(element.getId());
            element.setElement(rev.getEntity());
            element.setRevisionNumber(rev.getRevisionNumber().intValue());
        } catch (JpaSystemException se) {
            System.out.println("PublicationElement - JpaSystemException");
            System.out.println(se.getMessage());
        } catch (Exception ex) {
            System.out.println("PublicationElement fill");
            ex.printStackTrace();
        }

        if (element.getElement() != null){
            element.setName(element.getElementAsEntity().getName());
            element.setVersion(element.getElementAsEntity().getVersion());
        }
        return element;
    }




}
