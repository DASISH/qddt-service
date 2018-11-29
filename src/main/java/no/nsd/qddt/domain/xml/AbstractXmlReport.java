package no.nsd.qddt.domain.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Stig Norland
 */
public class AbstractXmlReport {

    protected Map<UUID,String> fragments = new HashMap<>();

    protected String html5toXML(String html5){
        Pattern p = Pattern.compile("<[^>]*>",
            Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        return p.matcher(html5).appendReplacement(  ).replaceAll("");
    }

}
