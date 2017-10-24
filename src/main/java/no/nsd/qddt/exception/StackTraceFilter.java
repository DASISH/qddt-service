package no.nsd.qddt.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class StackTraceFilter {

    public static List<StackTraceElement> filter(StackTraceElement[] stacktrace){
        List<StackTraceElement> retval =
            Arrays.stream(stacktrace)
            .filter(stackTraceElement -> stackTraceElement.toString().contains("no.nsd"))
            .collect(Collectors.toList());
        retval.add(0,stacktrace[0]);
        return retval;
    }


    public static void println(StackTraceElement[] stacktrace){
        filter(stacktrace)
        .forEach(System.out::println);
    }

}
