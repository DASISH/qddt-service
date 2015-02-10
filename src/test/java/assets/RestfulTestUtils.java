package assets;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Contain all the stuff needed to run unit tests towards the API.
 *
 * @author Dag Østgulen Heradstveit
 */
public class RestfulTestUtils {

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

    @Autowired
    private void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
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
}