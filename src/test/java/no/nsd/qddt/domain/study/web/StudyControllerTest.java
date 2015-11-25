package no.nsd.qddt.domain.study.web;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.utils.RestfulTestUtils;
import no.nsd.qddt.QDDT;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dag Østgulen Heradstveit
 */
@Transactional
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class StudyControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;
    private RestfulTestUtils rest;

    @Autowired
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private String accessToken;

    @Autowired
    private StudyService studyService;

    private Study study;

    @Before
    public void setUp() {

//        super.setup();
//        super.getBeforeSecurityContext().createSecurityContext();

        rest = new RestfulTestUtils(mappingJackson2HttpMessageConverter);
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation))
                .addFilter(springSecurityFilterChain)
                .dispatchOptions(true)
                .build();

        accessToken = rest.getAccessToken(
                "admin@example.org",
                "password",
                "client",
                "password",
                "http://localhost:8080/oauth/token");

        study = new Study();
        study.setName("Test Study One");
        study = studyService.save(study);

//        super.getBeforeSecurityContext().destroySecurityContext();
    }

    @Test
    public void findByIdFailNotFoundTest() throws Exception {
        mvc.perform(get("/study/00000000-0000-0000-0000-000000000000").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }


    @Test
    public void findByIdTest() throws Exception {
        mvc.perform(get("/study/"+study.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(document("project-by-id",
                        responseFields(
                                fieldWithPath("id").description(UUID.class),
                                fieldWithPath("name").description(""),
                                fieldWithPath("created").description(""),
                                fieldWithPath("updated").description(""),
                                fieldWithPath("createdBy").description(""),
                                fieldWithPath("changeComment").description(""),
                                fieldWithPath("agency").description(Agency.class),
                                fieldWithPath("changeKind").description(""),
                                fieldWithPath("surveyProgram").description(""),
                                fieldWithPath("instruments").description(""),
                                fieldWithPath("topicGroups").description(""),
                                fieldWithPath("basedOnObject").description(""))
                ));
    }
}
