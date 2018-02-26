package no.nsd.qddt.domain.responsedomain.json;

import no.nsd.qddt.domain.BaseJsonEdit;
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
public class ResponseDomainJsonEdit  extends BaseJsonEdit {

    private String description;
    private ManagedRepresentationJsonEdit managedRepresentation;
    private String displayLayout;
    private Set<CommentJsonEdit> comments = new HashSet<>();
    private ResponseKind responseKind;
    private ResponseCardinality responseCardinality;

    public ResponseDomainJsonEdit() {
    }

    public ResponseDomainJsonEdit(ResponseDomain responseDomain) {
        super(responseDomain);
        if (responseDomain == null) return;
        setComments(responseDomain.getComments().stream().map(CommentJsonEdit::new).collect(Collectors.toSet()));
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


    @Enumerated(EnumType.STRING)
    public ResponseKind getResponseKind() {
        return responseKind;
    }
    private void setResponseKind(ResponseKind responseKind) {
        this.responseKind = responseKind;
    }


    @Embedded
    public ResponseCardinality getResponseCardinality() {
        return responseCardinality;
    }
    private void setResponseCardinality(ResponseCardinality responseCardinality) {
        this.responseCardinality = responseCardinality;
    }
}
