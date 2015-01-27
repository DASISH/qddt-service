package controller;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.Survey;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import utils.HttpMockAuthSession;
import utils.RestfulTestUtils;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Dag Østgulen Heradstveit
 */
@Transactional
@IntegrationTest("server.port:0")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class SurveyControllerTest {

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
//        session = HttpMockAuthSession.createSession(qddtUserDetailsService, "user");
        rest = new RestfulTestUtils(mappingJackson2HttpMessageConverter);
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .dispatchOptions(true)
                .build();
    }

    @Test
    public void findByIdFailTest() throws Exception {
        mvc.perform(get("/survey/10000"))//.session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByIdTest() throws Exception {
        mvc.perform(get("/survey/1"))//.session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void findOneTest() throws Exception {
        Survey survey = surveyService.findById(1L);

        // ctx:/survey/id
        mvc.perform(get("/survey/1")//.session(session)
                .contentType(rest.getContentType())
                .content(rest.json(survey)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.id", is(survey.getId().intValue())))
//                .andExpect(jsonPath("$.guid", is("null")))
//                .andExpect(jsonPath("$.created", is("null")))
//                .andExpect(jsonPath("$.createdBy", is("null")))
//                .andExpect(jsonPath("$.name", is("null")))
//                .andExpect(jsonPath("$.changeReason", is("null")))
//                .andExpect(jsonPath("$.changeComment", is("null")))
                .andExpect(jsonPath("$.surveyName", is(survey.getSurveyName())))
//                .andExpect(jsonPath("$.agency", is("null")))
                .andExpect(status().isOk());
    }
}