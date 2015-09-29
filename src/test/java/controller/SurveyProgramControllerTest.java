package controller;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.user.QDDTUserDetailsService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import assets.HttpMockAuthSession;
import assets.RestfulTestUtils;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Dag Østgulen Heradstveit
 */
@IntegrationTest("server.port:0")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class SurveyProgramControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mvc;
    private RestfulTestUtils rest;

    @Autowired
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private SurveyProgramService surveyProgramService;

    @Autowired
    private QDDTUserDetailsService qddtUserDetailsService;

    private MockHttpSession session;

    @Before
    public void setUp() {
        session = HttpMockAuthSession.createSession(qddtUserDetailsService, "admin@example.org");
        rest = new RestfulTestUtils(mappingJackson2HttpMessageConverter);
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .dispatchOptions(true)
                .build();
    }

    @Test
    public void findByIdFailTest() throws Exception {
        mvc.perform(get("/survey/-1").session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByIdTest() throws Exception {
        mvc.perform(get("/survey/1").session(session))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void findOneTest() throws Exception {
        SurveyProgram surveyProgram = surveyProgramService.findAll().get(0);

        // ctx:/survey/id
        mvc.perform(get("/survey/1").session(session)
                .contentType(rest.getContentType())
                .content(rest.json(surveyProgram)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.id", is(surveyProgram.getId())))
//                .andExpect(jsonPath("$.guid", is("null")))
//                .andExpect(jsonPath("$.created", is("null")))
//                .andExpect(jsonPath("$.createdBy", is("null")))
//                .andExpect(jsonPath("$.name", is("null")))
//                .andExpect(jsonPath("$.changeReason", is("null")))
//                .andExpect(jsonPath("$.changeComment", is("null")))
                .andExpect(jsonPath("$.name", is(surveyProgram.getName())))
//                .andExpect(jsonPath("$.agency", is("null")))
                .andExpect(status().isOk());
    }

    @Test
    public void addCommentTest() throws Exception {
        String c = "This is a test comment";

        Comment comment = new Comment();
        comment.setComment(c);

        mvc.perform(post("/survey/" + 1 + "/comment").session(session)
                .contentType(rest.getContentType())
                .content(rest.json(comment)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.comment", is(c)));
    }
}