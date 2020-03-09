package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.embedded.Version;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IUrnDDI {

    UUID getId();

    Version getVersion();

    String getAgency();
}
