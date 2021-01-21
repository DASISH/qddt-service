package no.nsd.qddt.domain.classes.exception;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple util to extract the resource id from an exception message.
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public class ExtractFromException {

    /**
     * Get the resource ID which for now is always the last "word" in the exception.
     * Its no need to parse this as a {@link java.lang.Number} as its REST.
     * @param exceptionMessage from the exception caster
     * @return a formatted version.
     */
    public static String extractUUID(String exceptionMessage) {
        if (exceptionMessage != null) {
            Matcher patternMatcher = Pattern.compile("[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}").matcher(exceptionMessage);
            if(patternMatcher.find()) {
                return patternMatcher.group();
            }
        }
        return "NA";
    }

    /**
     * Get the resource ID which for now is always the last "word" in the exception.
     * Its no need to parse this as a {@link java.lang.Number} as its REST.
     * @param exceptionMessage from the exception caster
     * @return a formatted version.
     */
    public static String extractMessage(String exceptionMessage) {
        if (exceptionMessage != null) {
            Matcher patternMatcher = Pattern.compile("[^:]+").matcher(exceptionMessage);
            if(patternMatcher.find()) {
                return patternMatcher.group();
            }
            return exceptionMessage.substring(exceptionMessage.lastIndexOf(":")+1);
        }
        return "NA";
    }


}
