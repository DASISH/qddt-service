import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ActiveProfiles;
import service.*;
import service.audit.ResponseDomainCodeServiceAuditTest;
import service.audit.SurveyProgramServiceAuditTest;
import utils.ExtractResourceIdFromExceptionTest;

@ActiveProfiles("test")
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ExtractResourceIdFromExceptionTest.class,
        ResponseDomainCodeServiceAuditTest.class,
        SurveyProgramServiceAuditTest.class,
        CommentServiceTest.class,
        QuestionServiceTest.class,
        ResponseDomainCodeHierarchyTest.class,
        ResponseDomainCodeServiceTest.class,
        UserServiceTest.class
})
public class QTTDTestSuite {
}
