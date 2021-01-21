package no.nsd.qddt.domain.classes.elementref;

import no.nsd.qddt.domain.classes.interfaces.BaseServiceAudit;
import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditService;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditService;
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
    private final ConceptAuditService conceptService;
    private final ControlConstructAuditService controlConstructService;
    private final InstrumentAuditService instrumentService;
    private final PublicationAuditService publicationService;
    private final QuestionItemAuditService questionItemService;
    private final ResponseDomainAuditService responseDomainService;
    private final StudyAuditService studyService;
    private final SurveyProgramAuditService surveyProgramService;
    private final TopicGroupAuditService topicGroupService;

    @Autowired
    public ElementServiceLoader( ConceptAuditService conceptService,
                                 ControlConstructAuditService controlConstructService,
                                 InstrumentAuditService instrumentService,
                                 PublicationAuditService publicationService,
                                 QuestionItemAuditService questionItemService,
                                 ResponseDomainAuditService responseDomainService,
                                 StudyAuditService studyService,
                                 SurveyProgramAuditService surveyProgramService,
                                 TopicGroupAuditService topicGroupService)
    {
        LOG.info("ElementServiceLoader -> " );
        this.controlConstructService = controlConstructService;
        this.instrumentService = instrumentService;
        this.publicationService = publicationService;
        this.questionItemService = questionItemService;
        this.responseDomainService = responseDomainService;
        this.studyService = studyService;
        this.surveyProgramService = surveyProgramService;
        this.topicGroupService = topicGroupService;
        this.conceptService = conceptService;

    }

    public BaseServiceAudit getService(ElementKind elementKind){
        LOG.info("get Service -> " + elementKind   );
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
                LOG.error( "ElementKind :" + elementKind.getClassName() + " not defined." );
                return null;
        }
    }
}
