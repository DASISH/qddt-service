package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
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
public class ConceptJsonEdit extends BaseJsonEdit {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private Set<ConceptJsonEdit> children = new HashSet<>();

    private Set<QuestionItemJsonEdit> questionItems = new HashSet<>();

    private Set<ConceptQuestionItemJson> conceptQuestionItems = new HashSet<>();

    private String label;

    private String description;

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
            setQuestionItems(concept.getConceptQuestionItems().stream().map(F-> new QuestionItemJsonEdit(
                    F.getQuestionItem())).collect(Collectors.toSet()));
            setConceptQuestionItems(concept.getConceptQuestionItems().stream().map(Q-> new ConceptQuestionItemJson(Q))
                .collect(Collectors.toSet()));
            setTopicRef(concept.getTopicRef());
        }catch (Exception ex){
            System.out.println("ConceptJsonEdit Exception");
            ex.printStackTrace();
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

    public Set<QuestionItemJsonEdit> getQuestionItems() {
        return questionItems;
    }

    public void setQuestionItems(Set<QuestionItemJsonEdit> questionItems) {
        this.questionItems = questionItems;
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
}
