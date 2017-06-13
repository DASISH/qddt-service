package no.nsd.qddt.domain.topicgroupquestionitem;

import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItemId;
import no.nsd.qddt.domain.questionItem.json.QuestionItemJsonView;

import javax.persistence.EmbeddedId;
import java.sql.Timestamp;

/**
 * @author Stig Norland
 */
public class TopicGroupQuestionItemJson {

    @EmbeddedId
    private ParentQuestionItemId id = new ParentQuestionItemId();

    private QuestionItemJsonView questionItem;

    private Integer questionItemRevision;

//    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp updated;


    public TopicGroupQuestionItemJson(TopicGroupQuestionItem q) {
        if (q == null) {
            System.out.println("ConceptQuestionItem is null");
            return;
        }
        setId(q.getId());
        setQuestionItem(new QuestionItemJsonView(q.getQuestionItem()));
        setQuestionItemRevision(q.getQuestionItemRevision());
        setUpdated(q.getUpdated());
    }

    public ParentQuestionItemId getId() {
        return id;
    }

    public void setId(ParentQuestionItemId id) {
        this.id = id;
    }

    public QuestionItemJsonView getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(QuestionItemJsonView questionItem) {
        this.questionItem = questionItem;
    }

    public Integer getQuestionItemRevision() {
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Integer questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}
