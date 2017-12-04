package no.nsd.qddt;

import no.nsd.qddt.domain.agency.AgencyServiceTest;
import no.nsd.qddt.domain.agency.web.AgencyControllerTest;
import no.nsd.qddt.domain.category.CategoryServiceTest;
import no.nsd.qddt.domain.category.audit.CategoryAuditServiceTest;
import no.nsd.qddt.domain.category.web.CategoryControllerTest;
import no.nsd.qddt.domain.comment.CommentServiceTest;
import no.nsd.qddt.domain.comment.web.CommentControllerTest;
import no.nsd.qddt.domain.commentable.CommentableServiceTest;
import no.nsd.qddt.domain.concept.ConceptServiceTest;
import no.nsd.qddt.domain.concept.audit.ConceptAuditServiceTest;
import no.nsd.qddt.domain.concept.web.ConceptControllerTest;
import no.nsd.qddt.domain.controlconstruct.ControlConstructServiceTest;
import no.nsd.qddt.domain.controlconstruct.audit.ControlConstructAuditServiceTest;
import no.nsd.qddt.domain.controlconstruct.web.ControlConstructControllerTest;
import no.nsd.qddt.domain.instruction.InstructionServiceTest;
import no.nsd.qddt.domain.instruction.audit.InstructionAuditServiceTest;
import no.nsd.qddt.domain.instruction.web.InstructionControllerTest;
import no.nsd.qddt.domain.instrument.InstrumentServiceTest;
import no.nsd.qddt.domain.instrument.audit.OthermaterialAuditServiceTest;
import no.nsd.qddt.domain.instrument.web.InstrumentControllerTest;
import no.nsd.qddt.domain.othermaterial.OtherMaterialServiceTest;
import no.nsd.qddt.domain.othermaterial.web.OtherMaterialControllerTest;
import no.nsd.qddt.domain.responsedomain.ResponseDomainServiceTest;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditServiceTest;
import no.nsd.qddt.domain.responsedomain.web.ResponseDomainControllerTest;
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

//import no.nsd.qddt.domain.code.CodeHierarchyTest;
//import no.nsd.qddt.domain.code.ResponseDomainCategoryServiceTest;
//import no.nsd.qddt.domain.code.audit.ResponseDomainCategoryAuditServiceTest;
//import no.nsd.qddt.domain.code.web.ResponseDomainCategoryControllerTest;

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
        CommentControllerTest.class,

        // ====== Commentable ======
        CommentableServiceTest.class,

        // ====== Concept ======
        ConceptServiceTest.class,
        ConceptAuditServiceTest.class,
        ConceptControllerTest.class,

        // ====== Group ======
//        GroupServiceTest.class,
//        GroupAuditServiceTest.class,
//        GroupControllerTest.class,

        // ====== Instruction ======
        InstructionServiceTest.class,
        InstructionAuditServiceTest.class,
        InstructionControllerTest.class,

        // ====== Instrument ======
        InstrumentServiceTest.class,
        OthermaterialAuditServiceTest.class,
        InstrumentControllerTest.class,

        // ====== InstrumentQuestion ======
        ControlConstructServiceTest.class,
        ControlConstructAuditServiceTest.class,
        ControlConstructControllerTest.class,

        // ====== OtherMaterial ======
        OtherMaterialServiceTest.class,
        OtherMaterialControllerTest.class,

//        // ====== Question ======
//        QuestionServiceTest.class,
//        QuestionAuditServiceTest.class,
//        QuestionControllerTest.class,


        // ====== ResponseDomain ======
        ResponseDomainServiceTest.class,
        ResponseDomainAuditServiceTest.class,
        ResponseDomainControllerTest.class,

        // ====== ResponseDomainCode ======
//        ResponseDomainCategoryServiceTest.class,
//        ResponseDomainCategoryAuditServiceTest.class,
//        ResponseDomainCategoryControllerTest.class,
//        CodeHierarchyTest.class,

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
