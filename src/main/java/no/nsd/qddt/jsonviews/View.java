package no.nsd.qddt.jsonviews;

/**
 * @author Stig Norland
 */
public class View {
    public interface Simple {}
    public interface Edit  extends Simple{}
    public interface Audit extends Edit {}
    public interface SimpleList extends Simple{}
    public interface AuditList extends Audit {}
    public interface EditList extends Edit {}

}
