import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ActiveProfiles;
import service.*;
import service.audit.ResponseDomainCodeServiceAuditTest;
import service.audit.SurveyServiceAuditTest;

@ActiveProfiles("test")
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ResponseDomainCodeServiceAuditTest.class,
        SurveyServiceAuditTest.class,
        CommentServiceTest.class,
        QuestionServiceTest.class,
        ResponseDomainCodeHierarchyTest.class,
        ResponseDomainCodeServiceTest.class,
        UserServiceTest.class
})
public class QTTDTestSuite {
}
