package no.nsd.qddt.exception;

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
        this.id = idFromExceptionmessage(exceptionMessage);
        this.url = url;
        this.exceptionMessage = exceptionMessage;
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

    /**
     * Get the resource ID which for now is always the last "word" in the exception.
     * Its no need to parse this as a {@link java.lang.Number} as its REST.
     * @param exceptionMessage from the exception caster
     * @return a formatted version.
     */
    private String idFromExceptionmessage(String exceptionMessage) {
        return exceptionMessage.substring(exceptionMessage.lastIndexOf(" ")+1);
    }
}