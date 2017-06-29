package no.nsd.qddt.domain.topicgroupquestionitem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItem;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItemId;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Stig Norland
 */

@Entity
@Audited
@Table(name = "TOPIC_QUESTION_ITEM")
@AssociationOverrides({
        @AssociationOverride(name = "id.topicGroup", joinColumns = @JoinColumn(name = "PARENT_ID")),
        @AssociationOverride(name = "id.questionItem", joinColumns = @JoinColumn(name = "QUESTIONITEM_ID"))
})
@NamedNativeQuery(name="AuditTopicQuestionItem", query = "SELECT id, updated, based_on_object, change_comment, change_kind, name, major, minor, version_label, responsedomain_revision, user_id, agency_id, question_id, responsedomain_id, based_on_revision " +
                "FROM question_item_aud " +
                "WHERE id =:id and rev = :rev; ",
                resultClass = QuestionItem.class)
public class TopicGroupQuestionItem  implements ParentQuestionItem, java.io.Serializable {

    private static final long serialVersionUID = -7261887449839337877L;

    @EmbeddedId
    private ParentQuestionItemId id = new ParentQuestionItemId();



    @JsonBackReference(value = "ConceptQuestionItemConceptRef")
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "PARENT_ID",insertable = false, updatable = false)
    private TopicGroup topicGroup;


    /*
    This is the reference to the current QuestionItem, it has to be here in order for the framework
    to map the reference from and to Concept/ConceptQuestionItem/QuestionItem
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
//    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp updated;


    public TopicGroupQuestionItem(ParentQuestionItemId id, Integer questionItemRevision){
        setId(id);
        setQuestionItemRevision(questionItemRevision);
    }

    public TopicGroupQuestionItem(TopicGroup topicGroup, QuestionItem questionItem) {
        setTopicGroup(topicGroup);
        setQuestionItem(questionItem);
    }

//    public TopicGroupQuestionItem(TopicGroup topicGroup, QuestionItem questionItem, Integer questionItemRevision) {
//        this(topicGroup,questionItem);
//        setQuestionItemRevision(questionItemRevision);
//    }


    public ParentQuestionItemId getId() {
        return id;
    }

    private void setId(ParentQuestionItemId id) {
        this.id = id;
    }

    public TopicGroup getTopicGroup() {
        return topicGroup;
    }

    private void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
        this.id.setParentId(topicGroup.getId());
    }


    public QuestionItem getQuestionItemLateBound() {
        return questionItemLateBound;
    }

    public void setQuestionItemLateBound(QuestionItem questionItemLateBound) {
        this.questionItemLateBound = questionItemLateBound;
        this.getId().setQuestionItemId(questionItemLateBound.getId());
    }

    public QuestionItem getQuestionItem() {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicGroupQuestionItem)) return false;

        TopicGroupQuestionItem that = (TopicGroupQuestionItem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (topicGroup != null ? !topicGroup.equals(that.topicGroup) : that.topicGroup != null) return false;
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

    public void makeNewCopy(Integer revision) {
     //TODO implement
    }


}
