package no.nsd.qddt.domain.concept.json;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItemJson;
import no.nsd.qddt.domain.refclasses.TopicRef;
import no.nsd.qddt.domain.user.UserJson;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConceptJsonEdit extends BaseJsonEdit {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private Set<ConceptJsonEdit> children = new HashSet<>();

    private Set<ConceptQuestionItemJson> conceptQuestionItems = new HashSet<>();

    private String label;

    private String description;

    private UserJson modifiedBy;

    private AgencyJsonView agency;

    private Set<CommentJsonEdit> comments = new HashSet<>();

    private TopicRef topicRef;

    public ConceptJsonEdit() {
    }


    public ConceptJsonEdit(Concept concept) {
        super(concept);
        try{
            setId(concept.getId());
            setName(concept.getName());
            setChildren(concept.getChildren().stream().map(F-> new ConceptJsonEdit(F)).collect(Collectors.toSet()));
            setComments(concept.getComments().stream().map(F-> new CommentJsonEdit(F)).collect(Collectors.toSet()));
            setDescription(concept.getDescription());
            setLabel(concept.getLabel());
            setAgency(new AgencyJsonView(concept.getAgency()));
            setModifiedBy(new UserJson(concept.getModifiedBy()));
            setConceptQuestionItems(
                    concept.getConceptQuestionItems().stream().map(Q-> new ConceptQuestionItemJson(Q)).collect(Collectors.toSet()));
            setTopicRef(concept.getTopicRef());
        }catch (Exception ex){
            System.out.println("ConceptJsonEdit Exception");
            System.out.println(ex.getMessage());
            System.out.println(concept);
        }
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

    public Set<ConceptJsonEdit> getChildren() {
        return children;
    }

    public void setChildren(Set<ConceptJsonEdit> children) {
        this.children = children;
    }

    public Set<ConceptQuestionItemJson> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(Set<ConceptQuestionItemJson> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public UserJson getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public void setModifiedBy(UserJson modifiedBy) {
        this.modifiedBy = modifiedBy;
    }


    @Override
    public AgencyJsonView getAgency() {
        return agency;
    }

    @Override
    public void setAgency(AgencyJsonView agency) {
        this.agency = agency;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    public void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public TopicRef getTopicRef() {
        return topicRef;
    }

    public void setTopicRef(TopicRef topicRef) {
        this.topicRef = topicRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptJsonEdit)) return false;

        ConceptJsonEdit that = (ConceptJsonEdit) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;
        return topicRef != null ? topicRef.equals(that.topicRef) : that.topicRef == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "ConceptJsonEdit (id=%s, name=%s, children=%s, conceptQuestionItems=%s, label=%s, description=%s, comments=%s, topicRef=%s)",
                this.id, this.name, this.children, this.conceptQuestionItems, this.label, this.description, this.comments, this.topicRef);
    }


}