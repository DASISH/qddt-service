package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.ManagedRepresentationJson;
import no.nsd.qddt.domain.embedded.ResponseCardinality;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface ResponseDomainJson {
    UUID getId();
    String getName();
    String getDescription();
    ManagedRepresentationJson getManagedRepresentation();
    String getDisplayLayout();
    ResponseKind getResponseKind();
    ResponseCardinality getResponseCardinality();
}
