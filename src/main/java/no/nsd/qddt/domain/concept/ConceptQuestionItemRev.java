package no.nsd.qddt.domain.concept;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class ConceptQuestionItemRev  {

    private UUID questionId;
    private QuestionItem questionItem;
    private Long questionItemRevision;

    public ConceptQuestionItemRev() {
    }

    public ConceptQuestionItemRev(UUID id,Long rev) {
        setQuestionId(id);
        setQuestionItemRevision(rev);
    }

    @Type(type="pg-uuid")
    @Column(name="QUESTIONITEM_ID", updatable = false,insertable = false)
    public UUID getQuestionId() {
        return questionId;
    }
    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }


    @Transient
    @JsonSerialize
    public QuestionItem getQuestionItem() {
        return questionItem;
    }
    public void setQuestionItem(QuestionItem element) {
        this.questionItem = element;
    }

    @Column(name = "QUESTIONITEM_REVISION")
    public Long getQuestionItemRevision() {
        if (questionItemRevision==null)
            questionItemRevision=0L;
        return questionItemRevision;
    }
    public void setQuestionItemRevision(Long questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
        if  (getQuestionItem() != null)
            getQuestionItem().getVersion().setRevision(questionItemRevision);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptQuestionItemRev)) return false;

        ConceptQuestionItemRev that = (ConceptQuestionItemRev) o;

/*         if (questionItemLateBound != null ? !questionItemLateBound.equals(that.questionItemLateBound) : that.questionItemLateBound != null)
            return false;
 */        return (questionItemRevision != null ? questionItemRevision.equals(that.questionItemRevision) : that.questionItemRevision != null);
        
    }

    @Override
    public int hashCode() {
        return questionId != null ? questionId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"ConceptQuestionItem\", " +
                "\"questionItemRevision\":" + (questionItemRevision == null ? "null" : "\"" + questionItemRevision + "\"") + 
                "}";
    }


}

    