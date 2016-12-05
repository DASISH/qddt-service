package no.nsd.qddt.domain.refclasses;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface Refs {
    String getName();

    void setName(String name);

    UUID getId();

    void setId(UUID id);

}
