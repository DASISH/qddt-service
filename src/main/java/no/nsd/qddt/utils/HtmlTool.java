package no.nsd.qddt.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Stig Norland
 */
public class HtmlTool {

    public String toHtml(String source) {
        return source;
    }

    public String StripScript( String source) {
        return source;
    }

    private List<String> parseTxt(String source) {
        return Arrays.asList( source.split( "/n" ));
    }

}
