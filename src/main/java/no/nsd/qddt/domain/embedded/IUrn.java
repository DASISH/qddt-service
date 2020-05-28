package no.nsd.qddt.domain.embedded;

import no.nsd.qddt.domain.agency.Agency;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IUrn {

    UUID getId();

    Version getVersion();

    Agency getAgency();
}
