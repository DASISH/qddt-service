package no.nsd.qddt.domain.classes.exception;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

/**
 * @author Stig Norland
 */
public class DescendantsArchivedException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 5534742135508502155L;
    private static final Logger logger = LoggerFactory.getLogger(DescendantsArchivedException.class);

    public DescendantsArchivedException(String name) {
        super("Element has Archived Descendants, unable to remove.",null, true,false);
        logger.error("Element has Archived Descendants, unable to remove " + name);
    }
}
