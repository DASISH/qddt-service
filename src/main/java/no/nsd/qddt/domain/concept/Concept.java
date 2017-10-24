package no.nsd.qddt.domain.concept;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Archivable;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.refclasses.TopicRef;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
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
public class Concept extends AbstractEntityAudit implements Archivable {


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


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.MERGE}, mappedBy = "concept")
    private Set<ConceptQuestionItem> conceptQuestionItems = new HashSet<>(0);


    private String label;


    @Column(name = "description", length = 10000)
    private String description;


    @Transient
    @JsonDeserialize
    private TopicRef topicRef;

    private boolean isArchived;

    public Concept() {
//        System.out.println("CSTR Concept");
    }


    private TopicGroup getTopicGroup() {
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

    public void addConceptQuestionItem(ConceptQuestionItem conceptQuestionItem) {
        if (this.conceptQuestionItems.stream().noneMatch(cqi->conceptQuestionItem.getId().equals(cqi.getId()))) {
            if (conceptQuestionItem.getQuestionItem() != null){
                conceptQuestionItem.getQuestionItem().setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
                conceptQuestionItem.getQuestionItem().setChangeComment("Concept association added");
            }
            conceptQuestionItems.add(conceptQuestionItem);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation added");
        }
        else
            System.out.println("ConceptQuestionItem not inserted, match found" );
    }



    public void addQuestionItem(QuestionItem questionItem) {
        if (this.conceptQuestionItems.stream().noneMatch(cqi->questionItem.getId().equals(cqi.getId().getQuestionItemId()))) {
            new ConceptQuestionItem(this,questionItem);
            System.out.println("New QuestionItem added to ConceptQuestionItems " + questionItem.getName() );
            questionItem.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION
);
            questionItem.setChangeComment("Concept assosiation added");
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION
);
            this.setChangeComment("QuestionItem assosiation added");
        }
    }

    public  void removeQuestionItem(UUID qiId){
        getConceptQuestionItems().stream().filter(q -> q.getId().getQuestionItemId().equals(qiId)).
            forEach(cq->{
                System.out.println("removing qi from Concept->" + cq.getQuestionItem().getId());
                cq.getQuestionItem().setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION
);
                cq.getQuestionItem().setChangeComment("Concept assosiation removed");
                this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION
);
                this.setChangeComment("QuestionItem assosiation removed");
            });
        getConceptQuestionItems().removeIf(q -> q.getId().getQuestionItemId().equals(qiId));
    }


    public Set<Concept> getChildren() {
        return children;
    }

    public void setChildren(Set<Concept> children) {
        this.children = children;
    }

    public void addChildren(Concept concept){
        this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION
);
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


    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        isArchived = archived;
        if (archived) {
            System.out.println("Concept archived " + getName());
            setChangeKind(ChangeKind.ARCHIVED);
            for (Concept concept : getChildren()) {
                if (!concept.isArchived())
                    concept.setArchived(archived);
            }
        }
    }


    public TopicRef getTopicRef() {
        if (topicRef == null) {
            TopicGroup topicGroup = findTopicGroup2();
            if (topicGroup == null) {
//                System.out.println("getTopicRef IsNull -> " + this.getName());
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
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        super.makeNewCopy(revision);
        getConceptQuestionItems().forEach(q-> q.setConcept(this));
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
        return "{\"Concept\":"
                + super.toString()
                + ", \"conceptQuestionItems\":" + conceptQuestionItems
                + ", \"label\":\"" + label + "\""
                + ", \"description\":\"" + description + "\""
                + ", \"children\":" + children
                + "}";
    }


    @Override
    public String toDDIXml(){
        return super.toDDIXml();
    }


    @PreRemove
    private void removeReferencesFromConcept(){
        System.out.println("Concept pre remove");
//        getConceptQuestionItems().clear();
//        getConceptQuestionItems().forEach(cqi->cqi.getQuestionItem().updateStatusQI(this));
    }


    @Override
    public void fillDoc(PdfReport pdfReport) throws IOException {
        Document document =pdfReport.getTheDocument();
        document.add(new Paragraph(this.getName()).addStyle(pdfReport.getStyle()));
        pdfReport.addHeader(this);
        document.add(new Paragraph("Description"));
        document.add(new Paragraph(this.getDescription()));


        for (Comment item : this.getComments().stream().filter(c->c.isPublic()).collect(Collectors.toList())) {
            item.fillDoc(pdfReport);
        }
        document.add(new Paragraph()
                .setFont(pdfReport.getParagraphFont())
                .add("QuestionItem(s)"));
        for (ConceptQuestionItem item : getConceptQuestionItems()) {
            item.getQuestionItem().fillDoc(pdfReport);
        }

    }

}
