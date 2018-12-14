package no.nsd.qddt.domain;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IEntityAuditXmlRef  {
    UUID getId();
    Version getVersion();
    String getName();
    Agency getAgency();
    AbstractXmlBuilder getXmlBuilder();
}
