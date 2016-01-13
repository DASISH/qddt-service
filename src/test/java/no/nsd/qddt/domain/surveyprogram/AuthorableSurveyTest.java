package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.AuthorService;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.UserService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Stig Norland
 */
public class AuthorableSurveyTest extends AbstractServiceTest {

    @Autowired
    private SurveyProgramService surveyProgramService;

    @Autowired
    private SurveyProgramRepository surveyProgramRepository;

    @Autowired
    private AuthorService userService;

    @Before
    public void setup() {
        super.setup();
        super.setBaseRepositories(surveyProgramRepository);
    }

//    @After
//    @Override
//    public void tearDown() {
//        List<SurveyProgram> surveyList = surveyProgramRepository.findAll();
//        for (SurveyProgram ag :surveyList ) {
//            surveyProgramRepository.delete(ag);
//        }
//    }
    @Test
    @Override
    public void testCount() throws Exception {

        Author user1 = new Author();
            user1.setName("TestUser1");
            user1.setEmail("test1@example.org");
        user1 = userService.save(user1);

        Author user2 = new Author();
            user2.setName("TestUser1");
            user2.setEmail("test1@example.org");
        user2 = userService.save(user2);

//        List<SurveyProgram> surveyList = new List<SurveyProgram>();
        SurveyProgram survey =  new SurveyProgram();
        survey.setName("TEST AUTHORS");
        survey = surveyProgramService.save(survey );

//        survey.addAuthor(user1);
//        survey.addAuthor(user2);
//        surveyList.add(survey);

        survey.setChangeKind(AbstractEntityAudit.ChangeKind.NEW_MINOR);
        survey = surveyProgramService.save(survey );
        user1.addSurvey(survey);
        userService.save(user1);
        user2.addSurvey(survey);
        userService.save(user2);
        assertThat("Should be 2L ", surveyProgramService.findOne(survey.getId()).getAuthors().size(), is(2));


    }

    @Test
    @Override
    public void testExists() throws Exception {

    }

    @Test
    @Override
    public void testFindOne() throws Exception {

    }

    @Test
    @Override
    public void testSave() throws Exception {

    }

    @Test
    @Override
    public void testSaveAll() throws Exception {

    }

    @Test
    @Override
    public void testDelete() throws Exception {

    }

    @Test
    @Override
    public void testDeleteAll() throws Exception {

    }
}
