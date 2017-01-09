package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.questionItem.QuestionItemJsonEdit;
import no.nsd.qddt.domain.refclasses.TopicRef;
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

    private Set<QuestionItemJsonEdit> questionItems = new HashSet<>();

    private String label;

    private String description;

    private Set<Comment> comments = new HashSet<>();

    private TopicRef topicRef;

    public ConceptJsonEdit() {
    }

    public ConceptJsonEdit(Concept concept) {
        super(concept);
        setId(concept.getId());
        setName(concept.getName());
        setChildren(concept.getChildren().stream().map(F-> new ConceptJsonEdit(F)).collect(Collectors.toSet()));
        setComments(concept.getComments());
        setDescription(concept.getDescription());
        setLabel(concept.getLabel());
        setQuestionItems(concept.getQuestionItems().stream().map(F-> new QuestionItemJsonEdit(F)).collect(Collectors.toSet()));
        setTopicRef(concept.getTopicRef());
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

    public Set<QuestionItemJsonEdit> getQuestionItems() {
        return questionItems;
    }

    public void setQuestionItems(Set<QuestionItemJsonEdit> questionItems) {
        this.questionItems = questionItems;
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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public TopicRef getTopicRef() {
        return topicRef;
    }

    public void setTopicRef(TopicRef topicRef) {
        this.topicRef = topicRef;
    }
}
