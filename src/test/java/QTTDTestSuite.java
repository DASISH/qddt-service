import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // Add test classes here
        // ATestClass.class
})
public class QTTDTestSuite {
}
