package no.nsd.qddt.domain.conceptquestionitem;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Embeddable
public class ParentQuestionItemId implements Serializable {

    private static final long serialVersionUID = -7261887879839337877L;

    @Type(type="pg-uuid")
    @Column(name = "PARENT_ID")
    private UUID parentId;

    @Type(type="pg-uuid")
    @Column(name = "QUESTIONITEM_ID")
    private UUID questionItemId;


    public ParentQuestionItemId() {
    }

    public ParentQuestionItemId(Concept concept, QuestionItem questionItem) {
        this.setParentId(concept.getId());
        this.setQuestionItemId(questionItem.getId());
    }

    public ParentQuestionItemId(TopicGroup topicGroup, QuestionItem questionItem) {
        this.setParentId(topicGroup.getId());
        this.setQuestionItemId(questionItem.getId());
    }

    public ParentQuestionItemId(UUID parentId, UUID questionItemId) {
        this.questionItemId = questionItemId;
        this.parentId = parentId;
    }

    public UUID getQuestionItemId() {
        return questionItemId;
    }

    public void setQuestionItemId(UUID questionItemId) {
        this.questionItemId = questionItemId;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Id{" +
                ", ParentId=" + parentId +
                " QuestionId=" + questionItemId +
                '}';
    }
}
