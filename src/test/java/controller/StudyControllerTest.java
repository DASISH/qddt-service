package controller;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.service.QDDTUserDetailsService;
import no.nsd.qddt.service.SurveyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import assets.HttpMockAuthSession;
import assets.RestfulTestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dag Østgulen Heradstveit
 */
@Transactional
@IntegrationTest("server.port:0")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class StudyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;
    private RestfulTestUtils rest;

    @Autowired
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    QDDTUserDetailsService qddtUserDetailsService;

    private MockHttpSession session;

    @Before
    public void setUp() {
        session = HttpMockAuthSession.createSession(qddtUserDetailsService, "admin@example.org");
        rest = new RestfulTestUtils(mappingJackson2HttpMessageConverter);
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .dispatchOptions(true)
                .build();
    }

    @Test
    public void findByIdFailTest() throws Exception {
        mvc.perform(get("/study/-1").session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByIdTest() throws Exception {
        mvc.perform(get("/study/1").session(session))
                .andExpect(status().isOk());
    }
}
