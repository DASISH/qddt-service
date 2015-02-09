package utils;

import no.nsd.qddt.utils.ExtractResourceIdFromException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * @author Dag Østgulen Heradstveit
 */
@RunWith(JUnit4.class)
public class ExtractResourceIdFromExceptionTest {

    @Test
    public void extractResourceIdTest() throws Exception {
        String data = "Could not find resource with id 1";
        String output = ExtractResourceIdFromException.extract(data);

        assertEquals("Expected id 1", output, "1");
    }

    @Test
    public void extractEmailTest() throws Exception {
        String input = "This is a test of a email@example.com to check if it fetches the expected data.";
        String output = ExtractResourceIdFromException.extract(input);

        assertEquals("Expected an email.", output, "email@example.com");
    }
}
