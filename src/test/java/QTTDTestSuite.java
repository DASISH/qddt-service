import domain.comment.CommentServiceTest;
import domain.question.QuestionServiceTest;
import domain.responsedomain.audit.ResponseDomainCodeAuditServiceTest;
import domain.responsedomaincode.ResponseDomainCodeHierarchyTest;
import domain.responsedomaincode.ResponseDomainCodeServiceTest;
import domain.surveyprogram.audit.SurveyProgramAuditServicTest;
import domain.user.UserServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ActiveProfiles;
import utils.ExtractResourceIdFromExceptionTest;

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
