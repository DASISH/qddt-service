package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * General exception to catch all resources not found by id.
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);

    public ResourceNotFoundException(Number id, Class<?> clazz) {
        super("Could not find " + clazz.getSimpleName() + " with id " + id);
        logger.error("Could not find " + clazz.getSimpleName() + " with id " + id);
    }

    public ResourceNotFoundException(UUID id, Class<?> clazz) {
        super("Could not find " + clazz.getSimpleName() + " with id " + id.toString());
        logger.error("Could not find " + clazz.getSimpleName() + " with id " + id.toString());
    }

    public ResourceNotFoundException(String name, Class<?> clazz) {
        super("Could not find " + clazz.getSimpleName() + " with name " + name);
        logger.error("Could not find " + clazz.getSimpleName() + " with name " + name);
    }



}