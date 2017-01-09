package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.CategoryJsonEdit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ResponseDomainJsonView {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private String description;

    private CategoryJsonEdit managedRepresentation;

    private String displayLayout;

    private Set<Comment> comments = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private ResponseKind responseKind;

    @Embedded
    private ResponseCardinality responseCardinality;

    public ResponseDomainJsonView() {
    }

    public ResponseDomainJsonView(ResponseDomain responseDomain) {
        if (responseDomain == null) return;
        setId(responseDomain.getId());
        setName(responseDomain.getName());
        setComments(responseDomain.getComments());
        setDescription(responseDomain.getDescription());
        setDisplayLayout(responseDomain.getDisplayLayout());
        setManagedRepresentation(new CategoryJsonEdit(responseDomain.getManagedRepresentation()));
        setResponseCardinality(responseDomain.getResponseCardinality());
        setResponseKind(responseDomain.getResponseKind());
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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
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
