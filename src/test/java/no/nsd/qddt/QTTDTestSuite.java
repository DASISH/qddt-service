package no.nsd.qddt;

import no.nsd.qddt.domain.comment.CommentServiceTest;
import no.nsd.qddt.domain.question.QuestionServiceTest;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainCodeAuditServiceTest;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeHierarchyTest;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeServiceTest;
import no.nsd.qddt.domain.surveyprogram.audit.SurveyProgramAuditServicTest;
import no.nsd.qddt.domain.user.UserServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ActiveProfiles;
import no.nsd.qddt.utils.ExtractResourceIdFromExceptionTest;

@ActiveProfiles("test")
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ExtractResourceIdFromExceptionTest.class,
        ResponseDomainCodeAuditServiceTest.class,
        SurveyProgramAuditServicTest.class,
        CommentServiceTest.class,
        QuestionServiceTest.class,
        ResponseDomainCodeHierarchyTest.class,
        ResponseDomainCodeServiceTest.class,
        UserServiceTest.class
})
public class QTTDTestSuite {
}
