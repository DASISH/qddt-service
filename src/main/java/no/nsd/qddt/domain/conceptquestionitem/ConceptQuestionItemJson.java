package no.nsd.qddt.domain.conceptquestionitem;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.questionItem.json.QuestionItemJsonView;
import no.nsd.qddt.utils.JsonDateSerializer;

import javax.persistence.EmbeddedId;
import java.sql.Timestamp;

/**
 * @author Stig Norland
 */
public class ConceptQuestionItemJson {

    @EmbeddedId
    private ConceptQuestionItemId id = new ConceptQuestionItemId();

    private QuestionItemJsonView questionItem;

    private Integer questionItemRevision;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp updated;


    public ConceptQuestionItemJson(ConceptQuestionItem q) {
        setId(q.getId());
        setQuestionItem(new QuestionItemJsonView(q.getQuestionItem()));
        setQuestionItemRevision(q.getQuestionItemRevision());
        setUpdated(q.getUpdated());
    }

    public ConceptQuestionItemId getId() {
        return id;
    }

    public void setId(ConceptQuestionItemId id) {
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
