package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dag Østgulen Heradstveit.
 */
public class FileUploadException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 7324952042963154126L;
    private static final Logger logger = LoggerFactory.getLogger(FileUploadException.class);

    public FileUploadException(String path) {
        super("Unable to upload file");
        logger.error("Unable to upload file to location " + path);
    }
}
