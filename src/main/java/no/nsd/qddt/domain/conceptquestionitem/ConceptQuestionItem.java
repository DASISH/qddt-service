package no.nsd.qddt.domain.conceptquestionitem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Stig Norland
 */

@Entity
@Audited
@Table(name = "CONCEPT_QUESTION_ITEM")
@AssociationOverrides({
        @AssociationOverride(name = "id.concept", joinColumns = @JoinColumn(name = "PARENT_ID")),
        @AssociationOverride(name = "id.questionItem", joinColumns = @JoinColumn(name = "QUESTIONITEM_ID"))
})
//@NamedNativeQuery(name="AuditQuestionItem", query = "SELECT id, updated, based_on_object, change_comment, change_kind, name, major, minor, version_label, responsedomain_revision, user_id, agency_id, question_id, responsedomain_id, based_on_revision " +
//                "FROM question_item_aud " +
//                "WHERE id =:id and rev = :rev; ",
//                resultClass = QuestionItem.class)
public class ConceptQuestionItem  implements ParentQuestionItem,java.io.Serializable {

    private static final long serialVersionUID = -7261887349839337877L;

    @EmbeddedId
    @NotAudited
    private ParentQuestionItemId id = new ParentQuestionItemId();


    @JsonBackReference(value = "ConceptQuestionItemConceptRef")
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "PARENT_ID",insertable = false, updatable = false)
    private Concept concept;


    /*
    This is the reference to the current QuestionItem, it has to be here in order for the framework
    to map the reference from and to Concept<->ConceptQuestionItem<->QuestionItem
     */
    @JsonIgnore
    @JsonBackReference(value = "ConceptQuestionItemQuestionItemRef")
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "QUESTIONITEM_ID",insertable = false, updatable = false)
    private QuestionItem questionItemLateBound;


    /*
    This is the versioned QuestionItem, it has to be filled in manually, by the fetching service
     */
    @Transient
    @JsonSerialize
    @JsonDeserialize
    private QuestionItem questionItem;


    @Column(name = "QUESTIONITEM_REVISION")
    private Integer questionItemRevision;

    @Version
    @Column(name = "updated")
    private Timestamp updated;

    public ConceptQuestionItem() {
    }

    public ConceptQuestionItem(ParentQuestionItemId id, Integer questionItemRevision){
        setId(id);
        setQuestionItemRevision(questionItemRevision);
    }

    public ConceptQuestionItem(Concept concept, QuestionItem questionItem) {
        setConcept(concept);
        setQuestionItem(questionItem);
    }

    public ConceptQuestionItem(Concept concept, QuestionItem questionItem, Integer questionItemRevision) {
        this(concept,questionItem);
        setQuestionItemRevision(questionItemRevision);
    }


    public ParentQuestionItemId getId() {
        return id;
    }

    private void setId(ParentQuestionItemId id) {
        this.id = id;
    }


    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
        this.id.setParentId(concept.getId());
    }

    /*
    current version of QI, usually we'id use early bound.
     */
    public QuestionItem getQuestionItemLateBound() {
        return questionItemLateBound;
    }

    public void setQuestionItemLateBound(QuestionItem questionItemLateBound) {
        this.questionItemLateBound = questionItemLateBound;
        this.getId().setQuestionItemId(questionItemLateBound.getId());
    }

    public QuestionItem getQuestionItem() {
        if (questionItemLateBound != null && questionItem != null) {
            questionItem.setConceptRefs(questionItemLateBound.getConceptRefs());
        }
        return questionItem;
    }

    public void setQuestionItem(QuestionItem questionItem) {
        this.getId().setQuestionItemId(questionItem.getId());
        if (questionItemRevision == null && questionItem.getVersion().getRevision() != null)
            setQuestionItemRevision(questionItem.getVersion().getRevision());
        this.questionItem = questionItem;
    }


    public Integer getQuestionItemRevision() {
        if (questionItemRevision==null)
            questionItemRevision=0;
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Integer questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
        if  (getQuestionItem() != null)
            getQuestionItem().getVersion().setRevision(questionItemRevision);
    }


    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptQuestionItem)) return false;

        ConceptQuestionItem that = (ConceptQuestionItem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (concept != null ? !concept.equals(that.concept) : that.concept != null) return false;
        if (questionItemLateBound != null ? !questionItemLateBound.equals(that.questionItemLateBound) : that.questionItemLateBound != null)
            return false;
        if (questionItemRevision != null ? !questionItemRevision.equals(that.questionItemRevision) : that.questionItemRevision != null)
            return false;
        return updated != null ? updated.equals(that.updated) : that.updated == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "{\"_class\":\"ConceptQuestionItem\", " +
                "\"id\":" + (id == null ? "null" : id) + ", " +
                "\"questionItemLateBound\":" + (questionItemLateBound == null ? "null" : questionItemLateBound) + ", " +
                "\"questionItem\":" + (questionItem == null ? "null" : questionItem) + ", " +
                "\"questionItemRevision\":" + (questionItemRevision == null ? "null" : "\"" + questionItemRevision + "\"") + ", " +
                "\"updated\":" + (updated == null ? "null" : updated) +
                "}";
    }


    @PreRemove
    public void remove(){
        System.out.println("ConceptQuestionItem pre remove (do nothing");
        this.questionItem = null;
    }

    public void makeNewCopy(Integer revision) {
     //TODO implement
    }


}
