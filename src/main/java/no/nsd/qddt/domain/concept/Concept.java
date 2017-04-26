package no.nsd.qddt.domain.concept;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Pdfable;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.refclasses.TopicRef;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
public class Concept extends AbstractEntityAudit implements Commentable, Pdfable {


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REMOVE}, orphanRemoval = true)
    @OrderBy(value = "name asc")
    @JoinColumn(name = "parent_id")
    // Ordered arrayList doesn't work with Enver FIX
    @AuditMappedBy(mappedBy = "parentReferenceOnly")
    private Set<Concept> children = new HashSet<>();

    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    private Concept parentReferenceOnly;


    @JsonBackReference(value = "TopicGroupRef")
    @ManyToOne()
    @JoinColumn(name = "topicgroup_id",updatable = false)
    private TopicGroup topicGroup;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "CONCEPT_QUESTION_ITEM",
            joinColumns = {@JoinColumn(name = "concept_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "questionItem_id", referencedColumnName = "id" , updatable = false)})
    private Set<QuestionItem> questionItems = new HashSet<>();


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
    private TopicRef topicRef;

    public Concept() {

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
        return questionItems;
    }


    public void setQuestionItems(Set<QuestionItem> questions) {
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
        getQuestionItems().stream().filter(p->p.getId().equals(qiId)).
                findAny().ifPresent(qi -> {
                System.out.println("removing qi from Concept->" + qi.getId() );
                qi.getConcepts().remove(this);
                qi.setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
                qi.setChangeComment("Concept assosiation removed");
                this.questionItems.remove(qi);
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

    @Override
    public ByteArrayOutputStream makePdf() {

        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter( baosPDF));
        Document doc = new Document(pdf);
        fillDoc(doc);
        doc.close();
        return baosPDF;
    }

    @Override
    public void fillDoc(Document document) {

        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        document.add(new Paragraph("Survey Toc:").setFont(font));
        com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List()
                .setSymbolIndent(12)
                .setListSymbol("\u2022")
                .setFont(font);
        list.add(new ListItem(this.getName()));
        document.add(list);
        document.add(new Paragraph(this.getName()));
        document.add(new Paragraph(this.getModifiedBy() + "@" + this.getAgency()));
        document.add(new Paragraph(this.getDescription()));
        document.add(new Paragraph(this.getLabel()));
        document.add(new Paragraph(this.getTopicGroup().toString()));
        document.add(new Paragraph(this.getComments().toString()));

        for (QuestionItem item : getQuestionItems()) {
            item.fillDoc(document);
        }
    }
}

