package no.nsd.qddt.domain.interfaces;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IEntityRef {
    UUID getId();
    Version getVersion();
    String getName();
}
