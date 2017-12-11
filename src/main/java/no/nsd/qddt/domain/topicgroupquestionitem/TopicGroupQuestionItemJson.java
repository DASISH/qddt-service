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

    private Long questionItemRevision;

//    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp updated;


    public TopicGroupQuestionItemJson(TopicGroupQuestionItem q) {
        if (q == null) {
            System.out.println("TopicGroupQuestionItem is null");
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

    private void setId(ParentQuestionItemId id) {
        this.id = id;
    }

    public QuestionItemJsonView getQuestionItem() {
        return questionItem;
    }

    private void setQuestionItem(QuestionItemJsonView questionItem) {
        this.questionItem = questionItem;
    }

    public Long getQuestionItemRevision() {
        return questionItemRevision;
    }

    private void setQuestionItemRevision(Long questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    private void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}
