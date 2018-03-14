package no.nsd.qddt.utils;

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
        String data = "Could not find resource with id 2451d21a-27f1-4b35-82df-80cea1d0b4d6 ";
        String output = ExtractFromException.extractId(data);

        assertEquals("Expected id 2451d21a-27f1-4b35-82df-80cea1d0b4d6", output, "2451d21a-27f1-4b35-82df-80cea1d0b4d6");
    }

//    @Test
//    public void extractEmailTest() throws Exception {
//        String input = "This is a test of a email@example.com to check if it fetches the expected data.";
//        String output = ExtractFromException.extract(input);
//
//        assertEquals("Expected an email.", output, "email@example.com");
//    }
}
