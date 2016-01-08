package no.nsd.qddt.domain.SemVer;

import no.nsd.qddt.domain.version.SemVer;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Stig Norland
 */
public class SemVerTest {

    @Test
    public void testExists() throws Exception {

        SemVer ver = new SemVer();
        assertTrue(ver.toString().equals("0.0.0 "));

        ver.incMajor();
        assertTrue(ver.toString().equals("1.0.0 "));

        ver.setMinor();
        assertTrue(ver.toString().equals("1.1.0 "));

        ver.incPatch();
        assertTrue(ver.toString().equals("1.1.1 "));

        ver.setVersionLabel("TEST");
        assertTrue(ver.toString().equals("1.1.1 TEST"));

    }
}
