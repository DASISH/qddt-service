package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * General exception to catch all resources withouth their
 * wont exception handler.
 *
 * @author Dag Østgulen Heradstveit
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);

    public ResourceNotFoundException(Number id, Class<?> clazz) {
        super("Could not find " + clazz.getSimpleName() + " with id " + id);
        logger.error("Could not find " + clazz.getSimpleName() + " with id " + id);
    }
}