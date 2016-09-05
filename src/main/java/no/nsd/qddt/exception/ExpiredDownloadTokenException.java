package no.nsd.qddt.exception;


//import no.nsd.qddt.domain.downloadtoken.DownloadToken;
import no.nsd.qddt.domain.downloadtoken.DownloadToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpiredDownloadTokenException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ExpiredDownloadTokenException.class);


    public ExpiredDownloadTokenException(DownloadToken downloadToken) {
        super("Download token was expired. Expiration date " + downloadToken.getModified().toString());
        logger.error("Download token was expired. Expiration date " + downloadToken.getModified().toString());
    }

}