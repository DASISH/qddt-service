package no.nsd.qddt.domain.questionItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Pdfable;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;

import java.io.ByteArrayOutputStream;
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
public class QuestionItem extends AbstractEntityAudit implements Pdfable{

    /**
     * This field will be populated with the correct version of a RD,  but should never be persisted.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsedomain_id",insertable = false,updatable = false)
    private ResponseDomain responseDomain;

    /**
     * This field must be available "raw" in order to set and query  responsedomain by ID
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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "questionItem")
    private Set<ConceptQuestionItem> conceptQuestionItems = new HashSet<>(0);


    public QuestionItem() {

    }

    // Start pre remove ----------------------------------------------

    @PreRemove
    private void removeReferencesFromQi(){
        getConceptQuestionItems().forEach( CQ-> updateStatusQI(CQ.getConcept()));
        getConceptQuestionItems().clear();
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


    public Set<ConceptQuestionItem> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(Set<ConceptQuestionItem> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }


//    @Transient
//    @JsonSerialize
//    @JsonDeserialize
//    private Set<ConceptRef> conceptRefs = new HashSet<>();

    @Transient
    @JsonSerialize
    public Set<ConceptRef> getConceptRefs(){
        try {
            return conceptQuestionItems.stream().map(cq -> new ConceptRef(cq.getConcept())).collect(Collectors.toSet());
        } catch (Exception ex){
            ex.printStackTrace();
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

        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.add(new Paragraph("Survey Toc:").setFont(font));
        List list = new List()
                .setSymbolIndent(12)
                .setListSymbol("\u2022")
                .setFont(font);
        list.add(new ListItem(this.getName()));
        document.add(list);
        document.add(new Paragraph(this.getName()));
        document.add(new Paragraph(this.getModifiedBy() + "@" + this.getAgency()));
        document.add(new Paragraph(this.getResponseDomain().toString()));
        document.add(new Paragraph(this.getQuestion().toString()));
        document.add(new Paragraph(this.getComments().toString()));

        //    for (QuestionItem item : getQuestionItems()) {   item.fillDoc(document);  }
    }
}



