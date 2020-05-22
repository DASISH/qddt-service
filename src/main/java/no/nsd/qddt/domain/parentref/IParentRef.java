package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.embedded.Version;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IParentRef {
    UUID getId();
    Agency getAgency();
    Version getVersion();
    String getName();
    IRefs getParentRef();
}
