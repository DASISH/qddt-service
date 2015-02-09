package no.nsd.qddt.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple util to extract the resource id from an exception message.
 *
 * @author Dag Østgulen Heradstveit
 */
public class ExtractResourceIdFromException {

    /**
     * Get the resource ID which for now is always the last "word" in the exception.
     * Its no need to parse this as a {@link java.lang.Number} as its REST.
     * @param exceptionMessage from the exception caster
     * @return a formatted version.
     */
    public static String extract(String exceptionMessage) {

        if(exceptionMessage.contains("@")) {
            Matcher patternMatcher = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(exceptionMessage);
            if(patternMatcher.find()) {
                return patternMatcher.group();
            }
        }

        return exceptionMessage.substring(exceptionMessage.lastIndexOf(" ")+1);

    }
}
