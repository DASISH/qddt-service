package no.nsd.qddt.classes.elementref;

import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
import no.nsd.qddt.classes.interfaces.BaseServiceAudit;
import no.nsd.qddt.domain.publication.audit.PublicationAuditService;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
import no.nsd.qddt.domain.study.audit.StudyAuditService;
import no.nsd.qddt.domain.surveyprogram.audit.SurveyProgramAuditService;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Stig Norland
 */
@Service("elementServiceLoader")
public class ElementServiceLoader  {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final ControlConstructAuditService controlConstructService;
    private final InstrumentAuditService instrumentService;
    private final SurveyProgramAuditService surveyProgramService;
    private final StudyAuditService studyService;
    private final TopicGroupAuditService topicGroupService;
    private final ConceptAuditService conceptService;
    private final QuestionItemAuditService questionItemService;
    private final ResponseDomainAuditService responseDomainService;
    private final PublicationAuditService publicationService;

    @Autowired
    public ElementServiceLoader( ConceptAuditService conceptService,
                                 ControlConstructAuditService controlConstructService,
                                 InstrumentAuditService instrumentService,
                                 QuestionItemAuditService questionItemService,
                                 StudyAuditService studyService,
                                 SurveyProgramAuditService surveyProgramService,
                                 TopicGroupAuditService topicGroupService,
                                 ResponseDomainAuditService responseDomainService,
                                 PublicationAuditService publicationService)
    {
        this.conceptService = conceptService;
        this.controlConstructService = controlConstructService;
        this.instrumentService = instrumentService;
        this.questionItemService = questionItemService;
        this.studyService = studyService;
        this.surveyProgramService = surveyProgramService;
        this.topicGroupService = topicGroupService;
        this.responseDomainService = responseDomainService;
        this.publicationService = publicationService;
    }

    public BaseServiceAudit getService(ElementKind elementKind){
        switch (elementKind) {
            case CONCEPT:
                return conceptService;
            case CONTROL_CONSTRUCT:
            case QUESTION_CONSTRUCT:
            case STATEMENT_CONSTRUCT:
            case SEQUENCE_CONSTRUCT:
            case CONDITION_CONSTRUCT:
                return controlConstructService;
            case RESPONSEDOMAIN:
                return responseDomainService;
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
            case PUBLICATION:
                return publicationService;
            default:
                return null;
        }
    }
}
