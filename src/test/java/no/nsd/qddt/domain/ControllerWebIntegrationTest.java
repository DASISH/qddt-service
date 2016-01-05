package no.nsd.qddt.domain;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.agency.AgencyService;
import no.nsd.qddt.domain.user.UserService;
import no.nsd.qddt.utils.BeforeSecurityContext;
import no.nsd.qddt.utils.RestfulTestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

/**
 * Sets up the test environment for a controller test that
 * needs a {@link IntegrationTest} and Oauth2 tokens for headers
 * when testing secured resources.
 *
 * Example on simple controller test
 *
 *  @Test
 *  public void simpleTest() throws Exception {
 *      mvc.perform(get(RESOURCE-PATH).header("Authorization", "Bearer " + accessToken))
 *      .andExpect(status().isOk());
 *  }
 *
 * @author Dag Heradstveit
 */
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public abstract class ControllerWebIntegrationTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    public MockMvc mvc;
    public RestfulTestUtils rest;

    @Autowired
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    public  String accessToken;

    @Value("${test.username}")
    private String username;

    private BeforeSecurityContext beforeSecurityContext;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AgencyService agencyService;

    @Before
    public void setup() {
        this.beforeSecurityContext = new BeforeSecurityContext(authenticationManager);

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
    }

    public BeforeSecurityContext getBeforeSecurityContext() {
        return beforeSecurityContext;
    }


}
