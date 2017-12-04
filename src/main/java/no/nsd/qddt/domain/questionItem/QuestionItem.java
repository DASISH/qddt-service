package no.nsd.qddt.domain.questionItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.exception.StackTraceFilter;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.IOException;
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
public class QuestionItem extends AbstractEntityAudit {

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

    @Column(name = "question")
    private String questionText;

    @Column(name = "intent")
    private String questionIntent;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,  mappedBy = "questionItemLateBound") //cascade = {CascadeType.MERGE,CascadeType.DETACH},
    private Set<ConceptQuestionItem> conceptQuestionItems = new HashSet<>(0);

    @Transient
    @JsonSerialize
    private List<ConceptRef> conceptRefs;


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
        this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
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
        if (responseDomain!=null && (responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.BOOLEAN
                & responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.CATEGORY
                &  responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.DATETIME
                &  responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.NUMERIC
                &  responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.TEXT)
            & responseDomain.getManagedRepresentation().getChildren().isEmpty()){
            System.out.println(DateTime.now() + "MISSING ManagedRepresentation "
                    + responseDomain.getManagedRepresentation());
        }
            this.responseDomain = responseDomain;
        if (this.responseDomain != null)
            this.responseDomain.getVersion().setRevision(this.responseDomainRevision);
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


    public String getQuestion() {
        return questionText;
    }

    public void setQuestion(String questionText) {
        this.questionText = questionText;
    }

    public String getIntent() {
        return questionIntent;
    }

    public void setIntent(String questionIntent) {
        this.questionIntent = questionIntent;
    }

    private Set<ConceptQuestionItem> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    @SuppressWarnings("SameParameterValue")
    private void setConceptQuestionItems(Set<ConceptQuestionItem> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }


    public List<ConceptRef> getConceptRefs(){
        try {
            if (conceptRefs == null) {
                conceptRefs = conceptQuestionItems.stream()
                    .map(cq -> new ConceptRef(cq.getConcept()))
                    .sorted(ConceptRef::compareTo)
                    .collect(Collectors.toList());
            }
        } catch(Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            return new ArrayList<>();
        }
        return conceptRefs;
    }

    public void setConceptRefs(List<ConceptRef> conceptRefs) {
        this.conceptRefs = conceptRefs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionItem)) return false;
        if (!super.equals(o)) return false;

        QuestionItem that = (QuestionItem) o;

        if (responseDomainUUID != null ? !responseDomainUUID.equals(that.responseDomainUUID) : that.responseDomainUUID != null)
            return false;
        if (responseDomainRevision != null ? !responseDomainRevision.equals(that.responseDomainRevision) : that.responseDomainRevision != null)
            return false;
        if (questionText != null ? !questionText.equals(that.questionText) : that.questionText != null) return false;
        if (questionIntent != null ? !questionIntent.equals(that.questionIntent) : that.questionIntent != null)
            return false;
        return conceptRefs != null ? conceptRefs.equals(that.conceptRefs) : that.conceptRefs == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responseDomainUUID != null ? responseDomainUUID.hashCode() : 0);
        result = 31 * result + (responseDomainRevision != null ? responseDomainRevision.hashCode() : 0);
        result = 31 * result + (questionText != null ? questionText.hashCode() : 0);
        result = 31 * result + (questionIntent != null ? questionIntent.hashCode() : 0);
        result = 31 * result + (conceptRefs != null ? conceptRefs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QuestionItem{" + super.toString() +
                ", question='" + questionText + '\'' +
                ", responseDomain=" +  (responseDomain != null ? responseDomain.getName(): "?")  + '\'' +
                ", responseUUID=" + responseDomainUUID + '\'' +
                ", responseRev" + responseDomainRevision  + '\'' +
                "} " + System.lineSeparator();
    }

    @Override
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        super.makeNewCopy(revision);
        setConceptQuestionItems(null);
        getComments().clear();
        System.out.println("MADE NEW COPY...");
    }


    @Override
    public void fillDoc(PdfReport pdfReport,String counter) throws IOException {
        pdfReport.addHeader(this,"QuestionItem");
        pdfReport.addParagraph(this.getQuestion());
        if(!StringTool.IsNullOrTrimEmpty(getIntent())) {
            pdfReport.addheader2("Intent")
            .add(new Paragraph(this.getIntent()));
        }
        if (getResponseDomain() != null)
            this.getResponseDomain().fillDoc(pdfReport,"");
        pdfReport.addPadding();

        if(getComments().size()>0)
            pdfReport.addheader2("Comments");
        pdfReport.addComments(getComments());
        pdfReport.addPadding();
    }



}


