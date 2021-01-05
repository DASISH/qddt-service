package no.nsd.qddt.classes.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stig Norland
 */
public class ReferenceInUseException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ReferenceInUseException.class);

    public ReferenceInUseException(String name) {
        super("Unable to remove ");
        logger.error("Unable to remove " + name);
    }
}
