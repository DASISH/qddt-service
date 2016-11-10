package no.nsd.qddt.domain.questionItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;
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
public class QuestionItem extends AbstractEntityAudit  {


    @ManyToOne
    @JoinColumn(name = "responsedomain_id")
    private ResponseDomain responseDomain;

    @Column(name = "responsedomain_revision")
    private long responseDomainRevision;

    @ManyToOne(cascade =  {CascadeType.MERGE,CascadeType.DETACH},optional = false)
    @JoinColumn(name = "question_id")
    private Question question;


    @JsonBackReference(value = "conceptRef")
    @ManyToMany(mappedBy="questionItems")
    private Set<Concept> concepts = new HashSet<>();

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Set<ConceptRef> conceptRefs = new HashSet<>();
//    private Map<UUID,ConceptRef> conceptRefs = new HashMap<>();

    public QuestionItem() {

    }

    @PreRemove
    private void removeReferencesFromQi(){
        getConcepts().forEach( C-> updateStatusQI(C));
        getConcepts().clear();
        setResponseDomain(null);
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

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    public Set<ConceptRef> getConceptRefs(){
        try {
//        return concepts.stream().collect(Collectors.toMap(p-> p.getId(), c-> new ConceptRef(c)));
            return concepts.stream().map(c -> new ConceptRef(c)).collect(Collectors.toSet());
        } catch (Exception ex){
            return new HashSet<>(0);
        }
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


