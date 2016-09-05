package no.nsd.qddt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dag Østgulen Heradstveit.
 */
public class FileUploadException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadException.class);

    public FileUploadException(String path) {
        super("Unable to upload file");
        logger.error("Unable to upload file to location " + path);
    }
}
