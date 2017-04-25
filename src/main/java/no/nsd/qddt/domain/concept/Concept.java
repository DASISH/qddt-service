package no.nsd.qddt.domain.concept;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.controlconstruct.ControlConstructInstruction;
import no.nsd.qddt.domain.controlconstruct.ControlConstructInstructionRank;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.refclasses.TopicRef;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <ul class="inheritance">
 *     <li>A Concept consist of one or more QuestionItems.</li>
 *     <ul class="inheritance">
 *         <li>Every QuestionItem will have a Question.</li>
 *         <li>Every QuestionItem will have a ResponseDomain.</li>
 *     </ul>
 * </ul>
 * <br>
 * ConceptScheme: Concepts express ideas associated with objects and means of representing the concept
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "CONCEPT")
public class Concept extends AbstractEntityAudit {


    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    private Concept parentReferenceOnly;


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REMOVE})
    @OrderBy(value = "name asc")
    @JoinColumn(name = "parent_id")
    @AuditMappedBy(mappedBy = "parentReferenceOnly")
    private Set<Concept> children = new HashSet<>(0);


    @JsonBackReference(value = "TopicGroupRef")
    @ManyToOne()
    @JoinColumn(name = "topicgroup_id",updatable = false)
    private TopicGroup topicGroup;


    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE }, mappedBy = "concept")
    private Set<ConceptQuestionItem> conceptQuestionItems = new HashSet<>(0);


    @Transient
    @JsonSerialize
    @JsonDeserialize
    @OneToMany
    private Set<QuestionItem> questionItems = new HashSet<>(0);

    private String label;


    @Column(name = "description", length = 10000)
    private String description;


    @Transient
    @JsonDeserialize
    private TopicRef topicRef;

    public Concept() {

    }

    public TopicGroup getTopicGroup() {
        return topicGroup;
    }

    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }



    public Set<ConceptQuestionItem> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(Set<ConceptQuestionItem> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }



    public Set<QuestionItem> getQuestionItems() {
        return questionItems;
    }

    public void setQuestionItems(Set<QuestionItem> questionItems) {
        this.questionItems = questionItems;
    }

    public void addQuestionItem(QuestionItem questionItem) {
        if (this.conceptQuestionItems.stream().noneMatch(cqi->questionItem.getId().equals(cqi.getId().getQuestionItemId()))) {
            new ConceptQuestionItem(this,questionItem);
            System.out.println("New QuestionItem added to ConceptQuestionItems " + questionItem.getName() );
            questionItem.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
            questionItem.setChangeComment("Concept assosiation added");
            this.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
            this.setChangeComment("QuestionItem assosiation added");
        }
    }

    public  void removeQuestionItem(UUID qiId){
        getConceptQuestionItems().stream().filter(q -> q.getQuestionItem().getId().equals(qiId)).
            forEach(cq->{
                System.out.println("removing qi from Concept->" + cq.getQuestionItem().getId());
                cq.getQuestionItem().setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
                cq.getQuestionItem().setChangeComment("Concept assosiation removed");
                this.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
                this.setChangeComment("QuestionItem assosiation removed");
            });
        getConceptQuestionItems().removeIf(q -> q.getQuestionItem().getId().equals(qiId));
    }

    public  void removeQuestionItem(QuestionItem questionItem){
        removeQuestionItem(questionItem.getId());
    }



    public Set<Concept> getChildren() {
        return children;
    }

    public void setChildren(Set<Concept> children) {
        this.children = children;
    }

    public void addChildren(Concept concept){
        this.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
        setChangeComment("SubConcept added");
        this.children.add(concept);

    }



    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public TopicRef getTopicRef() {
        if (topicRef == null) {
            TopicGroup topicGroup = findTopicGroup2();
            if (topicGroup == null) {
                System.out.println("getTopicRef IsNull -> " + this.getName());
                topicRef = new TopicRef();
            } else
                topicRef = new TopicRef(topicGroup);
        }

        return topicRef;
    }

    private TopicGroup findTopicGroup2(){
        Concept current = this;
        while(current.parentReferenceOnly !=  null){
            current = current.parentReferenceOnly;
        }
        return current.getTopicGroup();
    }

    public void setTopicRef(TopicRef topicRef) {
        this.topicRef = topicRef;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept)) return false;
        if (!super.equals(o)) return false;

        Concept concept = (Concept) o;

        if (label != null ? !label.equals(concept.label) : concept.label != null) return false;
        return !(description != null ? !description.equals(concept.description) : concept.description != null);
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Concept{" +
                " children=" + (children!= null ?  children.size() : "0") +
                ", topicGroup=" + (topicGroup!=null ? topicGroup.getName() :"null") +
                ", questionItems=" + (conceptQuestionItems !=null ? conceptQuestionItems.size() :"0") +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", name='" + super.getName() + '\'' +
                ", id ='" + super.getId() + '\'' +
                "} ";
    }


    @Override
    public String toDDIXml(){
        return super.toDDIXml();
    }


    @Override
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        super.makeNewCopy(revision);
        getConceptQuestionItems().forEach(q->{
            q.setConcept(this);
        });
        getChildren().forEach(c->c.makeNewCopy(revision));
        if (parentReferenceOnly == null & topicGroup == null & topicRef != null) {
//            topicGroupId = getTopicRef().getId();
            System.out.println("infering topicgroup id " + getTopicRef().getId() );
        }
        getComments().clear();
    }


    public void merge(Concept changed){
        System.out.println("Concept.merge");
        if (!getName().equals(changed.getName()))
            setName(changed.getName());
//        if (!getConceptQuestionItems().equals(changed.getConceptQuestionItems())) {
//            for (ConceptQuestionItem cqi:getConceptQuestionItems()) {
//                ConceptQuestionItem finalCqi = cqi;
//                Optional<ConceptQuestionItem> tmp= changed.getConceptQuestionItems().stream().filter(c->c.getId().equals(finalCqi.getId())).findFirst();
//                if (!cqi.equals(tmp)) {
//                    cqi = tmp;
//                    System.out.println("ConceptQuestionItem changed in merge");
//                }
//            }
//        }
        if(!getChildren().equals(changed.getChildren()))
            setChildren(changed.getChildren());
        if(!getDescription().equals(changed.getDescription()))
            setDescription(changed.getDescription());
        if(!getLabel().equals(changed.getLabel()))
            setLabel(changed.getLabel());
        if(!getQuestionItems().equals(changed.questionItems))
            setQuestionItems(changed.questionItems);
        if(!getAgency().equals(changed.getAgency()))
            setAgency(changed.getAgency());
        if(!getBasedOnObject().equals(changed.getBasedOnObject()))
            setBasedOnObject(changed.getBasedOnObject());
        if(!getBasedOnRevision().equals(changed.getBasedOnRevision()))
            setBasedOnRevision(changed.getBasedOnRevision());
        if(!getChangeComment().equals(changed.getChangeComment()))
            setChangeComment(changed.getChangeComment());
        if(!getChangeKind().equals(changed.getChangeKind()))
            setChangeKind(changed.getChangeKind());
        if(!getVersion().equals(changed.getVersion()))
            setVersion(changed.getVersion());
        // these are set everytime we persist, but this function might be used in other settings, and copy must be complete.
        if(!getModified().equals(changed.getModified()))
            setModified(changed.getModified());
        if(!getModifiedBy().equals(changed.getModifiedBy()))
            setModifiedBy(changed.getModifiedBy());
    }


    /*
    fetches pre and post instructions and add them to ControlConstructInstruction
     */
    public void harvestQuestionItems() {
        if (conceptQuestionItems == null)
            conceptQuestionItems = new HashSet<>(0);

        System.out.println("conceptQuestionItems " +conceptQuestionItems.size());

        try {
            for (QuestionItem questionItem : getQuestionItems()) {
                addQuestionItem(questionItem);
            }
        }catch (Exception ex){
            System.out.println("harvestQuestionItems exception " + ex.getMessage());
            ex.printStackTrace();
        }

        children.forEach(c->c.harvestQuestionItems());
        System.out.println("conceptQuestionItems " +conceptQuestionItems.size());
    }

    /*
    this function is useful for populating ControlConstructInstructions after loading from DB
    */
    public void populateQuestionItems(){
        setQuestionItems(getConceptQuestionItems().stream()
            .map(cqi->cqi.getQuestionItem())
            .collect(Collectors.toSet()));
        children.forEach(c->c.populateQuestionItems());
    }

    @PreRemove
    private void removeReferencesFromConcept(){
        getConceptQuestionItems().forEach(cqi->cqi.getQuestionItem().updateStatusQI(this));
        getQuestionItems().clear();
        if (getTopicGroup() != null)
            getTopicGroup().removeConcept(this);

    }


}

