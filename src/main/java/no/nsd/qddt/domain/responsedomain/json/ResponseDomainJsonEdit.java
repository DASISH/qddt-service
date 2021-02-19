package no.nsd.qddt.domain.responsedomain.json;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.ResponseCardinality;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.category.json.ManagedRepresentationJsonEdit;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseKind;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ResponseDomainJsonEdit  extends AbstractJsonEdit {

    private String description;

    private ManagedRepresentationJsonEdit managedRepresentation;

    private String displayLayout;

    private List<CommentJsonEdit> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ResponseKind responseKind;

    @Embedded
    private ResponseCardinality responseCardinality;

    private String anchorLabel;

    public ResponseDomainJsonEdit() {
    }

    public ResponseDomainJsonEdit(ResponseDomain responseDomain) {
        super(responseDomain);
        if (responseDomain == null) return;
        setComments(responseDomain.getComments());
        setDescription(responseDomain.getDescription());
        setDisplayLayout(responseDomain.getDisplayLayout());
        setManagedRepresentation(new ManagedRepresentationJsonEdit(responseDomain.getManagedRepresentation()));
        List<Category> leafs = getLeafs( responseDomain.getManagedRepresentation() );
        int size = leafs.size();
        anchorLabel = managedRepresentation.getChildren().stream()
            .map( c ->  (size > 2 && c.getLabel().length() > 12 ) ? c.getLabel().substring( 0,9 )+"..": c.getLabel() )
            .collect( Collectors.joining(" + ") );
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

    public List<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(List<CommentJsonEdit> comments) {
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

    public String getAnchorLabel() {
        return anchorLabel;
    }

    private List<Category> getLeafs(Category root) {
        List<Category> children = new ArrayList<>( 2 );
        if (root.getCategoryType() == CategoryType.MIXED) {
            root.getChildren().forEach( c -> children.addAll( getLeafs( c ) ) );
        } else {
            children.addAll( root.getChildren() );
        }
        return children;
    }
}
