package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stig Norland
 */
public class RequestAbortedException extends RuntimeException{

    private static final long serialVersionUID = 513859607031911571L;
    private static final Logger logger = LoggerFactory.getLogger(InvalidObjectException.class);
    private static final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

    public RequestAbortedException(Exception ex) {
        super("Request " + stackTraceElements[2].getClassName() +"." + stackTraceElements[2].getMethodName() + " could not finish." + ex.getMessage(), ex.getCause() !=null ? ex.getCause():new Throwable(ex.getMessage()) );
        logger.error(stackTraceElements[2].getClassName() +"." + stackTraceElements[2].getMethodName() + "failed, message: " + ex.getMessage());
    }

    public RequestAbortedException(String message) {
        super(message );
        logger.error(stackTraceElements[2].getClassName() +"." + stackTraceElements[2].getMethodName() + "failed, message: " + message);

    }
}
