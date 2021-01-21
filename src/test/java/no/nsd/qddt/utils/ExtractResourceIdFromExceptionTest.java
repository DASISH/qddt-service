package no.nsd.qddt.utils;

import no.nsd.qddt.domain.classes.exception.ExtractFromException;
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
    public void extractResourceIdTest() {
        String data = "Could not find resource with id 2451d21a-27f1-4b35-82df-80cea1d0b4d6 ";
        String output = ExtractFromException.extractUUID(data);

        assertEquals("Expected id 2451d21a-27f1-4b35-82df-80cea1d0b4d6", output, "2451d21a-27f1-4b35-82df-80cea1d0b4d6");
    }

}
