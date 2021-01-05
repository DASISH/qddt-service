package no.nsd.qddt.classes.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dag Østgulen Heradstveit.
 */
public class FileDownloadException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 2602852740974424455L;
    private static final Logger logger = LoggerFactory.getLogger(FileDownloadException.class);

    public FileDownloadException(String path) {
        super("Attempted to download a file at an invalid location.");
        logger.error("Attempted to download a file at an invalid location " + path);
    }
}
