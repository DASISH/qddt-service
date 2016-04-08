package no.nsd.qddt.domain.study.web;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Dag Østgulen Heradstveit
 */
@Transactional
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class StudyControllerTest extends ControllerWebIntegrationTest {
//
//    @Rule
//    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mvc;
//    private RestfulTestUtils rest;
//
//    @Autowired
//    private HttpMessageConverter mappingJackson2HttpMessageConverter;
//
//    @Autowired
//    private FilterChainProxy springSecurityFilterChain;
//
//    private String accessToken;

    @Autowired
    private StudyService studyService;

    private Study study;

    @Override
    public void setup() {
        super.setup();
        super.getBeforeSecurityContext().createSecurityContext();

        study = new Study();
        study.setName("A test entity");
        study = studyService.save(study);

        super.getBeforeSecurityContext().destroySecurityContext();
    }

    @Test
    public void testGet() throws Exception {

        study = new Study();
        study.setName("A Get test entity");
        study = studyService.save(study);

        mvc.perform(get("/surveyprogram/"+study.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        study.setName(study.getName() + " edited");

        mvc.perform(post("/surveyprogram").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(study)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(study.getName())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        SurveyProgram aEntity = new SurveyProgram();
        aEntity.setName("Posted entity");

        mvc.perform(post("/surveyprogram/create").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aEntity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(aEntity.getName())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/surveyprogram/delete/"+study.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Instruction should no longer exist", studyService.exists(study.getId()));
    }


//    @Before
//    public void setUp() {
//
//        rest = new RestfulTestUtils(mappingJackson2HttpMessageConverter);
//        mvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(this.restDocumentation))
//                .addFilter(springSecurityFilterChain)
//                .dispatchOptions(true)
//                .build();
//
//        accessToken = rest.getAccessToken(
//                "admin@example.org",
//                "password",
//                "client",
//                "password",
//                "http://localhost:8080/oauth/token");
//
//        study = new Study();
//        study.setName("Test Study One");
//        study = studyService.save(study);
//
//    }
//
//    @Test
//    public void findByIdFailNotFoundTest() throws Exception {
//        mvc.perform(get("/study/00000000-0000-0000-0000-000000000000").header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isNotFound());
//    }
//
//
//    @Test
//    public void findByIdTest() throws Exception {
//        mvc.perform(get("/study/"+study.getId()).header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andDo(document("project-by-id",
//                        responseFields(
//                                fieldWithPath("id").description(UUID.class),
//                                fieldWithPath("name").description(""),
//                                fieldWithPath("created").description(""),
//                                fieldWithPath("updated").description(""),
//                                fieldWithPath("createdBy").description(""),
//                                fieldWithPath("changeComment").description(""),
//                                fieldWithPath("agency").description(Agency.class),
//                                fieldWithPath("changeKind").description(""),
//                                fieldWithPath("surveyProgram").description(""),
//                                fieldWithPath("instruments").description(""),
//                                fieldWithPath("topicGroups").description(""),
//                                fieldWithPath("basedOnObject").description(""))
//                ));
//    }
}
