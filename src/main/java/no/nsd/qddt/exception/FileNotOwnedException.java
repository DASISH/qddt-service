package no.nsd.qddt.exception;


import no.nsd.qddt.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class FileNotOwnedException extends RuntimeException {

    private static final long serialVersionUID = 297954013228247353L;
    private static final Logger logger = LoggerFactory.getLogger(FileNotOwnedException.class);

    public FileNotOwnedException(User activeUser, UUID fileId) {
        super("User:" + activeUser.getUsername() + " does now own data set with id " + fileId);
        logger.error("User:" + activeUser.getUsername() + " does now own data set with id " + fileId);
    }
}