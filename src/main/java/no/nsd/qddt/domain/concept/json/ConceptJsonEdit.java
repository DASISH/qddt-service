package no.nsd.qddt.domain.concept.json;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItemJson;
import no.nsd.qddt.domain.refclasses.TopicRef;
import no.nsd.qddt.exception.StackTraceFilter;
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

    private String label;

    private String description;

    private Set<ConceptQuestionItemJson> conceptQuestionItems = new HashSet<>();

    private Set<CommentJsonEdit> comments = new HashSet<>();

    private Set<ConceptJsonEdit> children = new HashSet<>();

    private boolean isArchived;

    private TopicRef topicRef;

    public ConceptJsonEdit() {
    }


    public ConceptJsonEdit(Concept concept) {
        super(concept);
        try{
            setId(concept.getId());
            setName(concept.getName());
            setChildren(concept.getChildren().stream().map(ConceptJsonEdit::new).collect(Collectors.toSet()));
            setComments(concept.getComments().stream().map(CommentJsonEdit::new).collect(Collectors.toSet()));
            setDescription(concept.getDescription());
            setLabel(concept.getLabel());
            setArchived(concept.isArchived());
//            setAgency(new AgencyJsonView(concept.getAgency()));
//            setModifiedBy(new UserJson(concept.getModifiedBy()));
            setConceptQuestionItems(
                    concept.getConceptQuestionItems().stream().map(ConceptQuestionItemJson::new).collect(Collectors.toSet()));
            setTopicRef(concept.getTopicRef());
        } catch (Exception ex) {
//            System.out.println(concept);
            LOG.error("ConceptJsonEdit exception",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
    }

    public UUID getId() {
        return id;
    }

    protected void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public Set<ConceptJsonEdit> getChildren() {
        return children;
    }

    private void setChildren(Set<ConceptJsonEdit> children) {
        this.children = children;
    }

    public Set<ConceptQuestionItemJson> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    private void setConceptQuestionItems(Set<ConceptQuestionItemJson> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }

    public String getLabel() {
        return label;
    }

    private void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public TopicRef getTopicRef() {
        return topicRef;
    }

    private void setTopicRef(TopicRef topicRef) {
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


    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
