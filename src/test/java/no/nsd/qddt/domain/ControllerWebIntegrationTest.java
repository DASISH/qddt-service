package no.nsd.qddt.domain;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.utils.RestfulTestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

/**
 * @author Dag Heradstveit
 */
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public class ControllerWebIntegrationTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    public MockMvc mvc;
    private RestfulTestUtils rest;

    @Autowired
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    public  String accessToken;

    @Value("${test.username}")
    private String username;

    @Before
    public void setup() {
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
}
