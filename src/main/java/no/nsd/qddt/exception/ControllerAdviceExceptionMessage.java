package no.nsd.qddt.exception;

/**
 * Default error message object. Can be used anywhere in the application where
 * exceptions or errors are being returned to the client over API requests.
 * For consistency, this is the only API error object the application should use.
 *
 * @author Dag Østgulen Heradstveit
 */
public class ControllerAdviceExceptionMessage {

    private String url;
    private String exceptionMessage;

    public ControllerAdviceExceptionMessage(String url, String exceptionMessage) {
        this.url = url;
        this.exceptionMessage = exceptionMessage;
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
}