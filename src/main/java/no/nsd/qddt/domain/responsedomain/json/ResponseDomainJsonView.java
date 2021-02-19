package no.nsd.qddt.domain.responsedomain.json;

import no.nsd.qddt.domain.ResponseCardinality;
import no.nsd.qddt.domain.category.json.ManagedRepresentationJsonView;
import no.nsd.qddt.domain.classes.interfaces.Version;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ResponseDomainJsonView {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private String description;

    private String displayLayout;

    private Version version;

    @Enumerated(EnumType.STRING)
    private ResponseKind responseKind;

    private ManagedRepresentationJsonView managedRepresentation;

    @Embedded
    private ResponseCardinality responseCardinality;

    private String classKind;

    public ResponseDomainJsonView() {
    }

    public ResponseDomainJsonView(ResponseDomain responseDomain) {
        if (responseDomain == null){
            LOG.info("ResponseDomainJsonView(NULL)");
            return;
        }
        setId(responseDomain.getId());
        setName(responseDomain.getName());
        setDescription(responseDomain.getDescription());
        setDisplayLayout(responseDomain.getDisplayLayout());
        setVersion(responseDomain.getVersion());
        setResponseCardinality(responseDomain.getResponseCardinality());
        setResponseKind(responseDomain.getResponseKind());
        setManagedRepresentation(new ManagedRepresentationJsonView(responseDomain.getManagedRepresentation()));
        classKind = responseDomain.getClassKind();
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

    public ManagedRepresentationJsonView getManagedRepresentation() {
        return managedRepresentation;
    }

    private void setManagedRepresentation(ManagedRepresentationJsonView managedRepresentation) {
        this.managedRepresentation = managedRepresentation;
    }
    
    public String getClassKind() {
        return classKind;
    }
}
