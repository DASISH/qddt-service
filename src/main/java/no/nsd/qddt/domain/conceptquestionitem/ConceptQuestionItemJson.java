package no.nsd.qddt.domain.conceptquestionitem;

import no.nsd.qddt.domain.questionItem.json.QuestionItemJsonView;

import javax.persistence.EmbeddedId;
import java.sql.Timestamp;

/**
 * @author Stig Norland
 */
public class ConceptQuestionItemJson {

    @EmbeddedId
    private ParentQuestionItemId id = new ParentQuestionItemId();

    private QuestionItemJsonView questionItem;

    private Integer questionItemRevision;

//    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp updated;


    public ConceptQuestionItemJson(ConceptQuestionItem q) {
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

    private void setId(ParentQuestionItemId id) {
        this.id = id;
    }

    public QuestionItemJsonView getQuestionItem() {
        return questionItem;
    }

    private void setQuestionItem(QuestionItemJsonView questionItem) {
        this.questionItem = questionItem;
    }

    public Integer getQuestionItemRevision() {
        return questionItemRevision;
    }

    private void setQuestionItemRevision(Integer questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    private void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}
