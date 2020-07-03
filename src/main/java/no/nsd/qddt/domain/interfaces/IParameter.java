package no.nsd.qddt.domain.interfaces;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IParameter {
    UUID getId();
    String getName();
    UUID getReferencedId();


}
