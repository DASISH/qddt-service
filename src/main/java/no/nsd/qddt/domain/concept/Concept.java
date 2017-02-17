package no.nsd.qddt.domain.concept;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.refclasses.TopicRef;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
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
public class Concept extends AbstractEntityAudit implements Commentable {


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REMOVE}, orphanRemoval = true)
    @OrderBy(value = "name asc")
    @JoinColumn(name = "parent_id")
    // Ordered arrayList doesn't work with Enver FIX
    @AuditMappedBy(mappedBy = "parentReferenceOnly")
    private Set<Concept> children = new HashSet<>(0);

    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    private Concept parentReferenceOnly;


    @JsonBackReference(value = "TopicGroupRef")
    @ManyToOne()
    @JoinColumn(name = "topicgroup_id",updatable = false)
    private TopicGroup topicGroup;



    @JsonIgnore
//    @JsonManagedReference(value="conceptQuestionItemsRef")
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.DETACH}, mappedBy = "concept")
    private Set<ConceptQuestionItem> conceptQuestionItems = new HashSet<>(0);

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Set<QuestionItem> questionItems = new HashSet<>(0);


    @Column(name = "label")
    private String label;

    @Column(name = "description", length = 10000)
    private String description;

    @PostLoad
    private void logComments(){
        if (comments.size() > 0)
        System.out.println(getName() + " " + comments.size() + "...");
    }


    @OneToMany(mappedBy = "ownerId" ,fetch = FetchType.EAGER)
    @NotAudited
    private Set<Comment> comments = new HashSet<>();


    @Transient
    @JsonDeserialize
//    @JsonSerialize
    private TopicRef topicRef;

    public Concept() {

    }

    public Set<ConceptQuestionItem> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(Set<ConceptQuestionItem> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }

    @PreRemove
    private void removeReferencesFromConcept(){
        getQuestionItems().forEach(qi->qi.updateStatusQI(this));
        getQuestionItems().clear();
        if (getTopicGroup() != null)
            getTopicGroup().removeConcept(this);

    }

    @Override
    public UUID getId() {
        return super.getId();
    }


    public TopicGroup getTopicGroup() {
        return topicGroup;
    }


    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }



    public Set<QuestionItem> getQuestionItems() {
        if (questionItems.size() <= 0){
//            System.out.println("conceptQuestionItems.stream().map");
            questionItems = conceptQuestionItems.stream().map(c -> c.getQuestionItem()).collect(Collectors.toSet());
        }
        return questionItems;
    }


    public void setQuestionItems(Set<QuestionItem> questions) {
//        questions.forEach(questionItem -> {
//            if (!conceptQuestionItems.stream().anyMatch(cqi-> cqi.getQuestionItem().getId().equals(questionItem.getId()))){
//                conceptQuestionItems.add(new ConceptQuestionItem(this,questionItem));
//                System.out.println("setQuestionItems add new cqi");
//            }
//        });
        this.questionItems = questions;
    }


    public void addQuestion(Question question) {
        QuestionItem qi = new QuestionItem();
        qi.setQuestion(question);
        this.addQuestionItem(qi);
    }


    public void addQuestionItem(QuestionItem questionItem) {
        if (!this.questionItems.contains(questionItem)) {
            questionItem.getConcepts().add(this);
            this.questionItems.add(questionItem);
            questionItem.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
            questionItem.setChangeComment("Concept assosiation added");
            this.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
            this.setChangeComment("QuestionItem assosiation added");
        }
    }


    public  void removeQuestionItem(UUID qiId){
        getConceptQuestionItems().stream().filter(p->p.getQuestionItem().getId().equals(qiId)).
                findAny().ifPresent(qi -> {
                System.out.println("removing qi from Concept->" + qi.getId() );
                qi.getQuestionItem().setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
                qi.getQuestionItem().setChangeComment("Concept assosiation removed");
//                this.conceptQuestionItems.remove(qi);
                this.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
                this.setChangeComment("QuestionItem assosiation removed");
            });
//        this.getQuestionItems().forEach(questionItem -> System.out.println(questionItem));
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


    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    private TopicGroup findTopicGroup2(){
        Concept current = this;
        while(current.parentReferenceOnly !=  null){
            current = current.parentReferenceOnly;
        }
        return current.getTopicGroup();
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

    public void setTopicRef(TopicRef topicRef) {
        this.topicRef = topicRef;
    }

    @Override
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        super.makeNewCopy(revision);
        getQuestionItems().forEach(q->{
            q.getConcepts().add(this);
            System.out.println(q.getName());
        });
        getChildren().forEach(c->c.makeNewCopy(revision));
        if (parentReferenceOnly == null & topicGroup == null & topicRef != null) {
//            topicGroupId = getTopicRef().getId();
            System.out.println("infering topicgroup id " + getTopicRef().getId() );
        }
        getComments().clear();
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
                ", questionItems=" + (questionItems !=null ? questionItems.size() :"0") +
                ", comments=" + (comments != null ? comments.size() :"0") +
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
}

