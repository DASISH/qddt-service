package no.nsd.qddt.exception;

import no.nsd.qddt.domain.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stig Norland
 */
public class InvalidObjectException extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(InvalidObjectException.class);

    public InvalidObjectException(AbstractEntity object) {
        super("Object was badly formed " + object.getClass().getName() + "'.");
        logger.error("Object was badly formed " + object.getClass().getName() + "'.");
    }
}
