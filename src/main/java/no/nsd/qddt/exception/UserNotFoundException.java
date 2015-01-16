package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Dag Østgulen Heradstveit
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(UserNotFoundException.class);

    public UserNotFoundException(String email) {
        super("Could not find User by email '" + email + "'.");
        logger.error("Could not find user by email " + email + ".");
    }
}