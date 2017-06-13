package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItemJson;
import no.nsd.qddt.domain.questionItem.json.QuestionItemJsonEdit;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class TopicQuestions {

    @Type(type="pg-uuid")
    private UUID id;

    private UUID parent;

    private Set<ConceptQuestionItem> questionItems = new HashSet<>();


    public TopicQuestions() {
    }

    public TopicQuestions(Concept concept) {
        try{
            setQuestionItems(concept.getConceptQuestionItems());
            setParent(concept.getTopicGroup().getId());
            setId(concept.getId());
        }catch (Exception ex){
            System.out.println("TopicQuestions Exception");
            ex.printStackTrace();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getParent() {
        return parent;
    }

    public void setParent(UUID parent) {
        this.parent = parent;
    }

    public Set<ConceptQuestionItem> getQuestionItems() {
        return questionItems;
    }

    public void setQuestionItems(Set<ConceptQuestionItem> questionItems) {
        this.questionItems = questionItems;
    }
}
