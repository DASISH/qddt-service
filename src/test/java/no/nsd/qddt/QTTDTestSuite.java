package no.nsd.qddt;

import no.nsd.qddt.domain.agency.AgencyServiceTest;
import no.nsd.qddt.domain.code.CodeServiceTest;
import no.nsd.qddt.domain.code.web.CodeControllerTest;
import no.nsd.qddt.domain.comment.CommentServiceTest;
import no.nsd.qddt.domain.commentable.CommentableServiceTest;
import no.nsd.qddt.domain.concept.ConceptServiceTest;
import no.nsd.qddt.domain.concept.audit.ConceptAuditServiceTest;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instruction.InstructionServiceTest;
import no.nsd.qddt.domain.instruction.audit.InstructionServiceAuditTest;
import no.nsd.qddt.domain.instruction.web.InstructionControllerTest;
import no.nsd.qddt.domain.instrument.InstrumentServiceTest;
import no.nsd.qddt.domain.instrument.audit.InstrumentAuditServiceTest;
import no.nsd.qddt.domain.instrument.web.InstrumentControllerTest;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestionServiceTest;
import no.nsd.qddt.domain.othermaterial.OtherMaterialServiceTest;
import no.nsd.qddt.domain.question.QuestionServiceTest;
import no.nsd.qddt.domain.responsedomain.ResponseDomainServiceTest;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainCodeAuditServiceTest;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeHierarchyTest;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeServiceTest;
import no.nsd.qddt.domain.study.StudyServiceTest;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramServiceTest;
import no.nsd.qddt.domain.surveyprogram.audit.SurveyProgramAuditServicTest;
import no.nsd.qddt.domain.topicgroup.TopicGroupServiceTest;
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

        // ====== Code ======
        CodeServiceTest.class,
        CodeControllerTest.class,

        // ====== Comment ======
        CommentServiceTest.class,

        // ====== Commentable ======
        CommentableServiceTest.class,

        // ====== Concept ======
        ConceptServiceTest.class,
        ConceptAuditServiceTest.class,

        // ====== Instruction ======
        InstructionServiceTest.class,
        InstructionControllerTest.class,
        InstructionServiceAuditTest.class,

        // ====== Instrument ======
        InstrumentServiceTest.class,
        InstrumentControllerTest.class,
        InstrumentAuditServiceTest.class,

        // ====== InstrumentQuestion ======
        InstrumentQuestionServiceTest.class,

        // ====== OtherMaterial ======
        OtherMaterialServiceTest.class,

        // ====== Question ======
        QuestionServiceTest.class,

        // ====== ResponseDomain ======
        ResponseDomainServiceTest.class,

        // ====== ResponseDomainCode ======
        ResponseDomainCodeHierarchyTest.class,
        ResponseDomainCodeAuditServiceTest.class,
        ResponseDomainCodeServiceTest.class,

        // ====== Study ======
        StudyServiceTest.class,

        // ====== SurveyProgram ======
        SurveyProgramServiceTest.class,
        SurveyProgramAuditServicTest.class,

        // ====== TopicGroup ======
        TopicGroupServiceTest.class,

        // ====== User ======
        UserServiceTest.class,

        // ====== Misc ======
        ExtractResourceIdFromExceptionTest.class,

})
public class QTTDTestSuite {
}
