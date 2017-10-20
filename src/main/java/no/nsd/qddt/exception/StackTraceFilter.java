package no.nsd.qddt.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class StackTraceFilter {

    public static List<StackTraceElement> filter(StackTraceElement[] stacktrace){
        return Arrays.stream(stacktrace)
           .filter(stackTraceElement -> stackTraceElement.getClassName().contains("nsd.no"))
                .collect(Collectors.toList());
    }


    public static void println(StackTraceElement[] stacktrace){
        filter(stacktrace)
        .forEach(System.out::println);
    }

}
