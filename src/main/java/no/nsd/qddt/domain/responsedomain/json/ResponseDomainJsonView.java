package no.nsd.qddt.domain.responsedomain.json;

import no.nsd.qddt.domain.category.json.CategoryJsonView;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.domain.embedded.Version;
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

    private Version version;

    @Enumerated(EnumType.STRING)
    private ResponseKind responseKind;

    private CategoryJsonView managedRepresentation;

    @Embedded
    private ResponseCardinality responseCardinality;

    public ResponseDomainJsonView() {
    }

    public ResponseDomainJsonView(ResponseDomain responseDomain) {
        if (responseDomain == null){
            System.out.println("ResponseDomainJsonView(NULL)");
            return;
        }
        setId(responseDomain.getId());
        setName(responseDomain.getName());
        setDescription(responseDomain.getDescription());
        setDisplayLayout(responseDomain.getDisplayLayout());
        setVersion(responseDomain.getVersion());
        setResponseCardinality(responseDomain.getResponseCardinality());
        setResponseKind(responseDomain.getResponseKind());
        setManagedRepresentation(new CategoryJsonView(responseDomain.getManagedRepresentation()));
    }

    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayLayout() {
        return displayLayout;
    }

    private void setDisplayLayout(String displayLayout) {
        this.displayLayout = displayLayout;
    }

    public Version getVersion() {
        return version;
    }

    private void setVersion(Version version) {
        this.version = version;
    }

    public ResponseKind getResponseKind() {
        return responseKind;
    }

    private void setResponseKind(ResponseKind responseKind) {
        this.responseKind = responseKind;
    }

    public ResponseCardinality getResponseCardinality() {
        return responseCardinality;
    }

    private void setResponseCardinality(ResponseCardinality responseCardinality) {
        this.responseCardinality = responseCardinality;
    }

    public CategoryJsonView getManagedRepresentation() {
        return managedRepresentation;
    }

    private void setManagedRepresentation(CategoryJsonView managedRepresentation) {
        this.managedRepresentation = managedRepresentation;
    }
}
