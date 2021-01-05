package no.nsd.qddt.classes.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dag Østgulen Heradstveit
 */
public class InvalidPasswordException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = -7026178953815161624L;
    private static final Logger logger = LoggerFactory.getLogger(InvalidPasswordException.class);

    public InvalidPasswordException(String email) {
        super("Invalid password for '" + email + "'.");
        logger.error("[logger] Invalid password for " + email + ".");
    }
}
