package no.nsd.qddt.domain;

/**
 * @author Stig Norland
 */
public interface Archivable {

    boolean isArchived();

    void setArchived(boolean archived);

}
