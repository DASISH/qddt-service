package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stig Norland
 */
public class ReferenceInUseException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(ReferenceInUseException.class);

    public ReferenceInUseException(String name) {
        super("Unable to remove file");
        logger.error("Unable to remove file  " + name);
    }
}
