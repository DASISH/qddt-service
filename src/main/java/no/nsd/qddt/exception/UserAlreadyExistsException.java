package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dag Østgulen Heradstveit
 */
public class UserAlreadyExistsException extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(UserAlreadyExistsException.class);

    public UserAlreadyExistsException(String email) {
        super("User already exsists '" + email + "'.");
        logger.error("User already exsists " + email + ".");
    }
}