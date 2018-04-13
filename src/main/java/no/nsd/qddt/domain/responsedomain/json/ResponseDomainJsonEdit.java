package no.nsd.qddt.domain.responsedomain.json;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.category.json.ManagedRepresentationJsonEdit;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseKind;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ResponseDomainJsonEdit  extends AbstractJsonEdit {

    private String description;

    private ManagedRepresentationJsonEdit managedRepresentation;

    private String displayLayout;

    private Set<CommentJsonEdit> comments = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private ResponseKind responseKind;

    @Embedded
    private ResponseCardinality responseCardinality;

    public ResponseDomainJsonEdit() {
    }

    public ResponseDomainJsonEdit(ResponseDomain responseDomain) {
        super(responseDomain);
        if (responseDomain == null) return;
        setComments(responseDomain.getComments());
        setDescription(responseDomain.getDescription());
        setDisplayLayout(responseDomain.getDisplayLayout());
        setManagedRepresentation(new ManagedRepresentationJsonEdit(responseDomain.getManagedRepresentation()));
        setResponseCardinality(responseDomain.getResponseCardinality());
        setResponseKind(responseDomain.getResponseKind());
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public ManagedRepresentationJsonEdit getManagedRepresentation() {
        return managedRepresentation;
    }

    private void setManagedRepresentation(ManagedRepresentationJsonEdit managedRepresentation) {
        this.managedRepresentation = managedRepresentation;
    }

    public String getDisplayLayout() {
        return displayLayout;
    }

    private void setDisplayLayout(String displayLayout) {
        this.displayLayout = displayLayout;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
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
}
