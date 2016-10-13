package no.nsd.qddt.domain.questionItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
public class QuestionItem extends AbstractEntityAudit  {


    @ManyToOne
    @JoinColumn(name = "responsedomain_id")
    private ResponseDomain responseDomain;

    @Column(name = "responsedomain_revision")
    private long responseDomainRevision;

    @ManyToOne(cascade =  {CascadeType.MERGE,CascadeType.DETACH},optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "question_revision")
    private long questionRevivsion;

    @JsonBackReference(value = "conceptRef")
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Concept> concepts = new HashSet<>();

    @JsonBackReference(value = "controlConstructRef")
    @OneToMany(mappedBy = "questionItem")
    private Set<ControlConstruct> controlConstructs = new HashSet<>();

    public QuestionItem() {

    }



    @PreRemove
    private void removeReferencesFromQi(){
        getConcepts().forEach( C-> updateStatusQI(C));
        getConcepts().clear();
        getControlConstructs().forEach(CC-> updateStatusControlConstruct(CC));
        getControlConstructs().clear();
        setResponseDomain(null);
    }

    private void updateStatusControlConstruct(ControlConstruct controlConstruct) {
        if (controlConstruct.getQuestionItem().equals(this)) {
            controlConstruct.getInstrument().setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
            controlConstruct.getInstrument().setChangeComment("QuestionContruct removed");
            controlConstruct.getInstrument().getControlConstructs().removeIf(CC -> CC.equals(controlConstruct));
            controlConstruct.setInstrument(null);
            controlConstruct.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
            controlConstruct.setChangeComment("Removed from instrument");
//            controlConstruct.setQuestionItem(null);
            setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
            setChangeComment("Removed from QuestionContruct");
        }
    }

    public void updateStatusQI(UUID conceptId) {
        concepts.forEach(C->{
            if (C.getId().equals(conceptId)) {
                updateStatusQI(C);
            }
        });
    }

    public void updateStatusQI(Concept concept) {
        this.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
        this.setChangeComment("Concept reference removed");
    }

    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
        this.responseDomain = responseDomain;
    }

    public long getResponseDomainRevision() {
        return responseDomainRevision;
    }

    public void setResponseDomainRevision(long responseDomainRevision) {
        this.responseDomainRevision = responseDomainRevision;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public long getQuestionRevivsion() {
        
        return questionRevivsion;
    }

    public void setQuestionRevivsion(long questionRevivsion) {
        this.questionRevivsion = questionRevivsion;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    public Set<ControlConstruct> getControlConstructs() {
        return controlConstructs;
    }

    public void setControlConstructs(Set<ControlConstruct> controlConstructs) {
        this.controlConstructs = controlConstructs;
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
        return "QuestionItem{" +
                ", question='" + question.getQuestion() + '\'' +
                ", responseDomain=" +  (responseDomain != null ? responseDomain.getName(): "?")  + '\'' +
                ", name='" + super.getName() + '\'' +
                ", id ='" + super.getId() + '\'' +
                "} " + System.lineSeparator();
    }

}


