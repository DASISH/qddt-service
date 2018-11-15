package no.nsd.qddt.utils;

/**
 * @author Stig Norland
 */
public class StringTool {

    public static String CapString(String input){
        return (input != null && input.length() > 1) ?
            input.substring( 0, 1 ).toUpperCase() + input.substring( 1 )
            : input;
    }

    public static boolean IsNullOrEmpty(String input){
        return (input == null || input.isEmpty());
    }


    public static boolean IsNullOrTrimEmpty(String input) {
        return (input == null || input.trim().isEmpty());
    }

    public static String SafeString(String input){
        return IsNullOrTrimEmpty(input)?"":input;
    }

    public static String likeify(String value){
        if (IsNullOrTrimEmpty(value)) return "";

        value = value.replace("*", "%");
        if (!value.startsWith("%"))
            value = "%"+value;
        if (!value.endsWith("%"))
            value = value + "%";
        value = value.replace("%%","%");
        return value;
    }
}
