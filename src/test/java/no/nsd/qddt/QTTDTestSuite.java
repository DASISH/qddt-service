package no.nsd.qddt;

import no.nsd.qddt.domain.agency.AgencyServiceTest;
import no.nsd.qddt.domain.agency.web.AgencyControllerTest;
import no.nsd.qddt.domain.category.CategoryServiceTest;
import no.nsd.qddt.domain.category.audit.CategoryAuditServiceTest;
import no.nsd.qddt.domain.category.web.CategoryControllerTest;
import no.nsd.qddt.domain.comment.CommentServiceTest;
import no.nsd.qddt.domain.comment.audit.CommentAuditServiceTest;
import no.nsd.qddt.domain.comment.web.CommentControllerTest;
import no.nsd.qddt.domain.commentable.CommentableServiceTest;
import no.nsd.qddt.domain.concept.ConceptServiceTest;
import no.nsd.qddt.domain.concept.audit.ConceptAuditServiceTest;
import no.nsd.qddt.domain.concept.web.ConceptControllerTest;
import no.nsd.qddt.domain.instruction.InstructionServiceTest;
import no.nsd.qddt.domain.instruction.audit.InstructionAuditServiceTest;
import no.nsd.qddt.domain.instruction.web.InstructionControllerTest;
import no.nsd.qddt.domain.instrument.InstrumentServiceTest;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditServiceTest;
import no.nsd.qddt.domain.instrument.web.InstrumentControllerTest;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestionServiceTest;
import no.nsd.qddt.domain.instrumentquestion.audit.InstrumentQuestionAuditServiceTest;
import no.nsd.qddt.domain.instrumentquestion.web.InstrumentQuestionControllerTest;
import no.nsd.qddt.domain.othermaterial.OtherMaterialServiceTest;
import no.nsd.qddt.domain.othermaterial.web.OtherMaterialControllerTest;
import no.nsd.qddt.domain.question.QuestionServiceTest;
import no.nsd.qddt.domain.question.audit.QuestionAuditServiceTest;
import no.nsd.qddt.domain.question.web.QuestionControllerTest;
import no.nsd.qddt.domain.responsedomain.ResponseDomainServiceTest;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditServiceTest;
import no.nsd.qddt.domain.responsedomain.web.ResponseDomainControllerTest;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeHierarchyTest;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCategoryServiceTest;
import no.nsd.qddt.domain.responsedomaincode.audit.ResponseDomainCategoryAuditServiceTest;
import no.nsd.qddt.domain.responsedomaincode.web.ResponseDomainCategoryControllerTest;
import no.nsd.qddt.domain.study.StudyServiceTest;
import no.nsd.qddt.domain.study.audit.StudyAuditServiceTest;
import no.nsd.qddt.domain.study.web.StudyControllerTest;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramServiceTest;
import no.nsd.qddt.domain.surveyprogram.audit.SurveyProgramAuditServicTest;
import no.nsd.qddt.domain.surveyprogram.web.SurveyProgramControllerTest;
import no.nsd.qddt.domain.topicgroup.TopicGroupServiceTest;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditServiceTest;
import no.nsd.qddt.domain.topicgroup.web.TopicGroupControllerTest;
import no.nsd.qddt.domain.user.UserServiceTest;
import no.nsd.qddt.utils.ExtractResourceIdFromExceptionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // ====== Agency ======
        AgencyServiceTest.class,
        AgencyControllerTest.class,

        // ====== Category ======
        CategoryServiceTest.class,
        CategoryAuditServiceTest.class,
        CategoryControllerTest.class,

        // ====== Comment ======
        CommentServiceTest.class,
        CommentAuditServiceTest.class,
        CommentControllerTest.class,

        // ====== Commentable ======
        CommentableServiceTest.class,

        // ====== Concept ======
        ConceptServiceTest.class,
        ConceptAuditServiceTest.class,
        ConceptControllerTest.class,

        // ====== Instruction ======
        InstructionServiceTest.class,
        InstructionAuditServiceTest.class,
        InstructionControllerTest.class,

        // ====== Instrument ======
        InstrumentServiceTest.class,
        InstrumentAuditServiceTest.class,
        InstrumentControllerTest.class,

        // ====== InstrumentQuestion ======
        InstrumentQuestionServiceTest.class,
        InstrumentQuestionAuditServiceTest.class,
        InstrumentQuestionControllerTest.class,

        // ====== OtherMaterial ======
        OtherMaterialServiceTest.class,
        OtherMaterialControllerTest.class,

        // ====== Question ======
        QuestionServiceTest.class,
        QuestionAuditServiceTest.class,
        QuestionControllerTest.class,


        // ====== ResponseDomain ======
        ResponseDomainServiceTest.class,
        ResponseDomainAuditServiceTest.class,
        ResponseDomainControllerTest.class,

        // ====== ResponseDomainCode ======
        ResponseDomainCategoryServiceTest.class,
        ResponseDomainCategoryAuditServiceTest.class,
        ResponseDomainCategoryControllerTest.class,
        ResponseDomainCodeHierarchyTest.class,

        // ====== Study ======
        StudyServiceTest.class,
        StudyAuditServiceTest.class,
        StudyControllerTest.class,

        // ====== SurveyProgram ======
        SurveyProgramServiceTest.class,
        SurveyProgramAuditServicTest.class,
        SurveyProgramControllerTest.class,

        // ====== TopicGroup ======
        TopicGroupServiceTest.class,
        TopicGroupAuditServiceTest.class,
        TopicGroupControllerTest.class,

        // ====== User ======
        UserServiceTest.class,

        // ====== Misc ======
        ExtractResourceIdFromExceptionTest.class,

})
public class QTTDTestSuite {
}
