package service.audit;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.Study;
import no.nsd.qddt.service.StudyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class StudyServiceAuditTest {

    @Autowired
    private StudyService studyService;

    @Test
    public void testCreateStudy() {
        Study study = new Study();
    }


}
