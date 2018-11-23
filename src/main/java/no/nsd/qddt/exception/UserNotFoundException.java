package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public class UserNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 3390953808347101016L;
    private static final Logger logger = LoggerFactory.getLogger(UserNotFoundException.class);

    public UserNotFoundException(String email) {
        super("Could not find User by email '" + email + "'.");
        logger.error("[logger] Could not find user by email " + email + ".");
        logger.debug(StackTraceFilter.nsdStack().toString());
    }

    public UserNotFoundException(UUID id ) {
        super("Could not find User by ID '" + id + "'.");
        logger.error("[logger] Could not find user by ID " + id + ".");
        logger.debug(StackTraceFilter.nsdStack().toString());
    }

}