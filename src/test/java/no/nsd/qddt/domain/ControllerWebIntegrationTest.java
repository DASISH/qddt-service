//package no.nsd.qddt.domain;
//
//import no.nsd.qddt.utils.RestfulTestUtils;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.FilterChainProxy;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//
///**
// * Sets up the test environment for a controller test that
// * needs a {@link IntegrationTest} and Oauth2 tokens for headers
// * when testing secured resources.
// *
// * Example on simple controller test
// *
// *
// *  public void simpleTest() throws Exception {
// *      mvc.perform(get(RESOURCE-PATH).header("Authorization", "Bearer " + accessToken))
// *      .andExpect(status().isOk());
// *  }
// *
// * @author Dag Heradstveit
// */
//@RunWith(SpringJUnit4ClassRunner)
//public abstract class ControllerWebIntegrationTest {
//
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    public MockMvc mvc;
//    public RestfulTestUtils rest;
//
//    @Autowired
//    private HttpMessageConverter mappingJackson2HttpMessageConverter;
//
//    @Autowired
//    private FilterChainProxy springSecurityFilterChain;
//
//    public  String accessToken;
//
//    @Value("${test.username}")
//    private String username;
//
//    private BeforeSecurityContext beforeSecurityContext;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    // @Autowired
//    // private UserService userService;
//
//    // @Autowired
//    // private AgencyService agencyService;
//
//    @Before
//    public void setup() {
//        this.beforeSecurityContext = new BeforeSecurityContext(authenticationManager);
//
//        rest = new RestfulTestUtils(mappingJackson2HttpMessageConverter);
//        mvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
////                .apply(documentationConfiguration(this.restDocumentation))
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
//    }
//
//    public BeforeSecurityContext getBeforeSecurityContext() {
//        return beforeSecurityContext;
//    }
//
//
//}
