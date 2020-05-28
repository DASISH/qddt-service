package no.nsd.qddt.domain.parentref;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IUrnDDI{

    UUID getId();

    String getVersion();

    String getAgency();
}
