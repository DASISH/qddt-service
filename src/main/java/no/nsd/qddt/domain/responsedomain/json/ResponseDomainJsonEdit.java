package no.nsd.qddt.domain.responsedomain.json;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.category.json.CategoryJsonEdit;
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

    private CategoryJsonEdit managedRepresentation;

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
        setComments(responseDomain.getComments().stream().map(F-> new CommentJsonEdit(F)).collect(Collectors.toSet()));
        setDescription(responseDomain.getDescription());
        setDisplayLayout(responseDomain.getDisplayLayout());
        setManagedRepresentation(new CategoryJsonEdit(responseDomain.getManagedRepresentation()));
        setResponseCardinality(responseDomain.getResponseCardinality());
        setResponseKind(responseDomain.getResponseKind());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryJsonEdit getManagedRepresentation() {
        return managedRepresentation;
    }

    public void setManagedRepresentation(CategoryJsonEdit managedRepresentation) {
        this.managedRepresentation = managedRepresentation;
    }

    public String getDisplayLayout() {
        return displayLayout;
    }

    public void setDisplayLayout(String displayLayout) {
        this.displayLayout = displayLayout;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    public void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
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
}
