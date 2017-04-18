package no.nsd.qddt.domain.responsedomain.json;

import no.nsd.qddt.domain.category.json.CategoryJsonView;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ResponseDomainJsonView {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private String description;

    private String displayLayout;

    @Enumerated(EnumType.STRING)
    private ResponseKind responseKind;

    private CategoryJsonView managedRepresentation;

    @Embedded
    private ResponseCardinality responseCardinality;

    public ResponseDomainJsonView() {
    }

    public ResponseDomainJsonView(ResponseDomain responseDomain) {
        if (responseDomain == null) return;
        setId(responseDomain.getId());
        setName(responseDomain.getName());
        setDescription(responseDomain.getDescription());
        setDisplayLayout(responseDomain.getDisplayLayout());
        setResponseCardinality(responseDomain.getResponseCardinality());
        setResponseKind(responseDomain.getResponseKind());
        setManagedRepresentation(new CategoryJsonView(responseDomain.getManagedRepresentation()));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayLayout() {
        return displayLayout;
    }

    public void setDisplayLayout(String displayLayout) {
        this.displayLayout = displayLayout;
    }


    public ResponseKind getResponseKind() {
        return responseKind;
    }

    public void setResponseKind(ResponseKind responseKind) {
        this.responseKind = responseKind;
    }

    public ResponseCardinality getResponseCardinality() {
        return responseCardinality;
    }

    public void setResponseCardinality(ResponseCardinality responseCardinality) {
        this.responseCardinality = responseCardinality;
    }

    public CategoryJsonView getManagedRepresentation() {
        return managedRepresentation;
    }

    public void setManagedRepresentation(CategoryJsonView managedRepresentation) {
        this.managedRepresentation = managedRepresentation;
    }
}
