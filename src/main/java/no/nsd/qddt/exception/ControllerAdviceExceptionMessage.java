package no.nsd.qddt.exception;

import no.nsd.qddt.utils.ExtractFromException;

/**
 * Default error message object. Can be used anywhere in the application where
 * exceptions or errors are being returned to the client over API requests.
 * For consistency, this is the only API error object the application should use.
 *
 * @author Dag Østgulen Heradstveit
 */
public class ControllerAdviceExceptionMessage {

    private String id;
    private String url;
    private String exceptionMessage;

    public ControllerAdviceExceptionMessage(String url, String exceptionMessage) {
        this.id = ExtractFromException.extractId(exceptionMessage);
        this.url = url;
        this.exceptionMessage  = ExtractFromException.extractMessage( exceptionMessage);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String toString() {
        return  " id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", exceptionMessage='" + exceptionMessage;
    }
}