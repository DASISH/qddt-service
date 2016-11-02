package no.nsd.qddt.utils.builders;

/**
 * @author Stig Norland
 */
public class StringTool {

    public static String CapString(String input){
        if (input != null && input.length() >1)
            return  input.substring(0,1).toUpperCase()  + input.substring(1);
        else
            return input;
    }

    public static boolean IsNullOrEmpty(String input){
        return (input == null || input.isEmpty());
    }


    public static boolean IsNullOrTrimEmpty(String input) {
        return (input == null || input.trim().isEmpty());
    }
}
