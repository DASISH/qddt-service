package no.nsd.qddt.domain.questionItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Question Item is a container for Question (text) and responsedomain
 * This entity introduce a breaking change into the model. it supports early binding of
 * of the containing entities, by also supplying a reference to their revision number.
 * This binding is outside the model which is defined here and used by the framework.
 * This means that when fetching its content it will need to query the revision part of this
 * system, when a revision number is specified.
 *
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "QUESTION_ITEM")
public class QuestionItem extends AbstractEntityAudit implements Commentable {


    /**
     * This field will be populated with the correct version of a RD,
     * but should never be persisted.
     */
    @ManyToOne
    @JoinColumn(name = "responsedomain_id",insertable = false,updatable = false)
    private ResponseDomain responseDomain;

    /**
     * This field must be available "raw" in order to set and query
     * responsedomain by ID
     */
    @JsonIgnore
    @Type(type="pg-uuid")
    @Column(name="responsedomain_id")
    private UUID responseDomainUUID;


    @Column(name = "responsedomain_revision")
    private Integer responseDomainRevision;

    @ManyToOne(cascade =  {CascadeType.MERGE,CascadeType.DETACH},optional = false)
    @JoinColumn(name = "question_id")
    private Question question;


    @JsonIgnore
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY,  mappedBy = "questionItem")
    private Set<ConceptQuestionItem> conceptQuestionItems = new HashSet<>(0);


    @OneToMany(mappedBy = "ownerId" ,fetch = FetchType.LAZY)
    @NotAudited
    private Set<Comment> comments = new HashSet<>();

    public QuestionItem() {

    }

    // Start pre remove ----------------------------------------------

    @PreRemove
    private void removeReferencesFromQi(){
        getConcepts().forEach( C-> updateStatusQI(C));
        getConcepts().clear();
        setResponseDomain(null);
    }

    public void updateStatusQI(Concept concept) {
        this.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
        this.setChangeComment("Concept reference removed");
    }

    public void updateStatusQI(UUID conceptId) {
        conceptQuestionItems.forEach(C->{
            if (C.getConcept().getId().equals(conceptId)) {
                updateStatusQI(C.getConcept());
            }
        });
    }

    // End pre remove ----------------------------------------------



    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
        this.responseDomain = responseDomain;
    }

    public Integer getResponseDomainRevision() {
        return responseDomainRevision == null? 0:responseDomainRevision;
    }

    public void setResponseDomainRevision(Integer responseDomainRevision) {
        this.responseDomainRevision = responseDomainRevision;
    }

    public UUID getResponseDomainUUID() {
        return responseDomainUUID;
    }

    public void setResponseDomainUUID(UUID responseDomainUUID) {
        this.responseDomainUUID = responseDomainUUID;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @JsonIgnore
    private Set<Concept> getConcepts() {
        return conceptQuestionItems.stream().map(cqi->cqi.getConcept()).collect(Collectors.toSet());
    }

    public Set<ConceptQuestionItem> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(Set<ConceptQuestionItem> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }


    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Set<ConceptRef> conceptRefs = new HashSet<>();

    public Set<ConceptRef> getConceptRefs(){
        try {
            return getConcepts().stream().map(c -> new ConceptRef(c)).collect(Collectors.toSet());
        } catch (Exception ex){
            ex.printStackTrace();
            return new HashSet<>(0);
        }
    }

    public Set<Comment> getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionItem)) return false;
        if (!super.equals(o)) return false;

        QuestionItem questionItem = (QuestionItem) o;

        if (responseDomain != null ? !responseDomain.equals(questionItem.responseDomain) : questionItem.responseDomain != null)
            return false;
        return !(question != null ? !question.equals(questionItem.question) : questionItem.question != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responseDomain != null ? responseDomain.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QuestionItem{" + super.toString() +
                ", question='" + question.getQuestion() + '\'' +
                ", responseDomain=" +  (responseDomain != null ? responseDomain.getName(): "?")  + '\'' +
                ", responseUUID=" + responseDomainUUID + '\'' +
                ", responseRev" + responseDomainRevision  + '\'' +
                "} " + System.lineSeparator();
    }

    @Override
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        super.makeNewCopy(revision);
        setQuestion(question.newCopyOf());
        setConceptQuestionItems(null);
        getComments().clear();
        System.out.println("MADE NEW COPY...");
    }


}


