package no.nsd.qddt.domain;

import no.nsd.qddt.domain.embedded.Version;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IEntityUrn {

    UUID getId();

    Version getVersion();

    String getAgency();
}
