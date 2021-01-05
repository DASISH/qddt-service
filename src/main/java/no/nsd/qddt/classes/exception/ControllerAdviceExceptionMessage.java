package no.nsd.qddt.classes.exception;

/**
 * Default error message object. Can be used anywhere in the application where
 * exceptions or errors are being returned to the client over API requests.
 * For consistency, this is the only API error object the application should use.
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public class ControllerAdviceExceptionMessage {

    private String id;
    private String url;
    private final String exceptionMessage;
    private String userfriendlyMessage;


    public ControllerAdviceExceptionMessage(String url, String exceptionMessage) {
        this.id = ExtractFromException.extractUUID(exceptionMessage);
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

    public String getUserfriendlyMessage() {
        return userfriendlyMessage;
    }

    public void setUserfriendlyMessage(String userfriendlyMessage) {
        this.userfriendlyMessage = userfriendlyMessage;
    }

    @Override
    public String toString() {
        return "{\"ControllerAdviceExceptionMessage\":{"
            + "\"id\":\"" + id + "\""
            + ", \"url\":\"" + url + "\""
            + ", \"exceptionMessage\":\"" + exceptionMessage + "\""
            + ", \"userfriendlyMessage\":\"" + userfriendlyMessage + "\""
            + "}}";
    }
}
