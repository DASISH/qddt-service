package no.nsd.qddt.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Contain all the stuff needed to run unit tests towards the API.
 * This includes retrieving tokens from the authorization server.
 *
 * @author Dag Østgulen Heradstveit
 */
public class RestfulTestUtils {

    private RestTemplate template = new RestTemplate();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    /**
     * Takes an {@link org.springframework.beans.factory.annotation.Autowired} instance of a
     * {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter} from the
     * calling unit test class using that class' context to inject it.
     * @param mappingJackson2HttpMessageConverter from the context of the calling unit test class
     */
    public RestfulTestUtils(HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    /**
     * Get the JSON string representation of an object.
     * @param input object parsed to JSON
     * @return {@link java.lang.String} representation of the object as JSON
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String json(Object input) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                input, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    public MediaType getContentType() {
        return contentType;
    }

    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    /**
     * Get an accessToken to use for Oauth2 authorization.
     *
     * @param username test user username
     * @param password test user password
     * @param clientId id of the oauth2 client (this application)
     * @param clientPassword password of the oauth2 client (this application)
     * @param authorizeServerUrl url of the server responsible of authorization
     * @return oauth2 token
     */
    public String getAccessToken(String username, String password, String clientId, String clientPassword, String authorizeServerUrl) {
        return Objects.requireNonNull( template.exchange(
            URI.create( authorizeServerUrl +
                "?grant_type=password" +
                "&client_id=" + clientId +
                "&client_secret=" + clientPassword +
                "&password=" + password +
                "&username=" + username +
                "&scope=write" +
                "&redirect_uri=http://localhost:3000/" ),
            HttpMethod.POST,
            new HttpEntity<String>( createBasicHeader( "client", "password" ) ),
            OAuth2AccessToken.class
        ).getBody() ).getTokenValue();
    }

    /**
     * Create a Base64 encoded header basic authentication string.
     * This is needed for access to the /oauth/token endpoint.
     *
     * @param username for the service
     * @param password for the service
     * @return a base64 encoded header auth string
     */
    private static HttpHeaders createBasicHeader(String username, String password){
        return new HttpHeaders(){
            private static final long serialVersionUID = -4163461301389642349L;

            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }
}
