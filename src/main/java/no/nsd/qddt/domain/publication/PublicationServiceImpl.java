package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.study.audit.StudyAuditService;
import no.nsd.qddt.domain.surveyprogram.audit.SurveyProgramAuditService;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.prepost.PreAuthorize;
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

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final PublicationRepository repository;
    private final ConceptAuditService conceptService;
    private final ControlConstructAuditService controlConstructService;
    private final InstrumentAuditService instrumentService;
    private final QuestionItemAuditService questionItemService;
    private final StudyAuditService studyService;
    private final SurveyProgramAuditService surveyProgramService;
    private final TopicGroupAuditService topicGroupService;
    private boolean showPrivate= true;

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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public Publication save(Publication instance) {
        try {
            return repository.save(instance);
        } catch (Exception ex){
            LOG.error("Publication save ->",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
            throw ex;
        }

    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public List<Publication> save(List<Publication> instances) {
        return repository.save(instances);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(UUID uuid) {
        repository.delete(uuid);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(List<Publication> instances) {
        repository.delete(instances);
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


    protected Publication prePersistProcessing(Publication instance) {
        return instance;
    }


    private Publication postLoadProcessing(Publication instance) {
        if (instance.getStatus().toLowerCase().contains("public")|| instance.getStatus().toLowerCase().contains("extern"))
            showPrivate = false;

        instance.getPublicationElements().forEach(this::fill);
        return instance;
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
        service.setShowPrivateComment(showPrivate);
        try {
            element.setElement(service.findRevision(
                    element.getId(),
                    element.getRevisionNumber().intValue())
                    .getEntity());

        } catch (RevisionDoesNotExistException e) {
            Revision rev = service.findLastChange(element.getId());
            element.setElement(rev.getEntity());
            element.setRevisionNumber(rev.getRevisionNumber().longValue());
        } catch (JpaSystemException se) {
            LOG.error("PublicationElement - JpaSystemException",se);
            StackTraceFilter.filter(se.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
        } catch (Exception ex) {
            LOG.error("PublicationElement - fill",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
        }

        if (element.getElementAsEntity() != null){
            element.setName(element.getElementAsEntity().getName());
            element.setVersion(element.getElementAsEntity().getVersion());
        }
        return element;
    }




}
