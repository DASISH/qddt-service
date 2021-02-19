package no.nsd.qddt.domain.classes.exception;

import no.nsd.qddt.domain.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stig Norland
 */
public class InvalidObjectException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 7509899931915065094L;
    private static final Logger logger = LoggerFactory.getLogger(InvalidObjectException.class);

    public InvalidObjectException(AbstractEntity object) {
        super("Object was badly formed ->" + object );
        logger.error("Object was badly formed " + object.getClass().getName() + "'.");
    }
}
