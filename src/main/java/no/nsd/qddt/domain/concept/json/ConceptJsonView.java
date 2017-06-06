package no.nsd.qddt.domain.concept.json;

import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItemJson;
import no.nsd.qddt.domain.questionItem.json.QuestionItemJsonEdit;
import no.nsd.qddt.domain.refclasses.TopicRef;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConceptJsonView {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private String label;

    private String description;

    private Set<ConceptQuestionItemJson> conceptQuestionItems = new HashSet<>();

    private Set<CommentJsonEdit> comments = new HashSet<>();

    private Set<ConceptJsonView> children = new HashSet<>();

    private TopicRef topicRef;


    public ConceptJsonView(Concept concept) {
        setId(concept.getId());
        setName(concept.getName());
        setLabel(concept.getLabel());
        setDescription(concept.getDescription());
        setChildren(concept.getChildren().stream().map(F-> new ConceptJsonView(F)).collect(Collectors.toSet()));
        setConceptQuestionItems(concept.getConceptQuestionItems().stream().map(Q-> new ConceptQuestionItemJson(Q))
                .collect(Collectors.toSet()));
        setComments(concept.getComments().stream().map(F-> new CommentJsonEdit(F)).collect(Collectors.toSet()));
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

    public Set<ConceptQuestionItemJson> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(Set<ConceptQuestionItemJson> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    public void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public Set<ConceptJsonView> getChildren() {
        return children;
    }

    public void setChildren(Set<ConceptJsonView> children) {
        this.children = children;
    }

    public TopicRef getTopicRef() {
        return topicRef;
    }

    public void setTopicRef(TopicRef topicRef) {
        this.topicRef = topicRef;
    }
}
