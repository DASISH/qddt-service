package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Dag Østgulen Heradstveit
 */


@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudyNotFoundException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(StudyNotFoundException.class);

    public StudyNotFoundException(String id) {
        super("Could not find Study with id " + id);
        logger.error("Could not find Survey with id " + id);
    }
}