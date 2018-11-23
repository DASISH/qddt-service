package no.nsd.qddt.exception;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

/**
 * @author Stig Norland
 */
public class DescendantsArchivedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DescendantsArchivedException.class);

    public DescendantsArchivedException(String name) {
        super("Element has Archived Descendants, unable to remove.");
        logger.error("Element has Archived Descendants, unable to remove " + name);
    }
}
