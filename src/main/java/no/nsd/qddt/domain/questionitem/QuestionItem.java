package no.nsd.qddt.domain.questionitem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.parentref.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class QuestionItem extends AbstractEntityAudit {


    @Transient
    @JsonSerialize
    @JsonDeserialize
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

    @Column(name = "responsedomain_name")
    private String responseDomainName;


    @Column(name = "question", length = 2000)
    private String question;

    @Column(name = "intent", length = 3000)
    private String intent;


    @Transient
    @JsonSerialize
    private List<ConceptRef> conceptRefs = new ArrayList<>( 0 );


    public QuestionItem() {
        super();
    }

    // Start pre remove ----------------------------------------------

    @PreRemove
    private void removeReferencesFromQi(){
        //getConceptQuestionItems().forEach( CQ-> updateStatusQI(CQ.getConcept()));
        //getConceptQuestionItems().clear();
        setResponseDomain(null);
    }

    public void updateStatusQI() {
        this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        this.setChangeComment("Concept reference removed");
    }

    // End pre remove ----------------------------------------------


    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
        if (responseDomain!=null && (
                responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.BOOLEAN
                & responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.CATEGORY
                &  responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.DATETIME
                &  responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.NUMERIC
                &  responseDomain.getManagedRepresentation().getCategoryType() != CategoryType.TEXT)
            & responseDomain.getManagedRepresentation().getChildren().isEmpty()){
            LOG.info("MISSING ManagedRepresentation "+ responseDomain.getManagedRepresentation());
        }
            this.responseDomain = responseDomain;
        if (this.responseDomain != null) {
            setResponseDomainName( responseDomain.getName() );
            this.responseDomain.getVersion().setRevision(this.responseDomainRevision);
        }
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

    public String getResponseDomainName() {
        if (responseDomainName == null && responseDomain != null ) {
            responseDomainName = responseDomain.getName();
        }
        return responseDomainName;
    }

    public void setResponseDomainName(String responseDomainName) {
        this.responseDomainName = responseDomainName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }


    public List<ConceptRef> getConceptRefs(){
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

        if (!Objects.equals( responseDomainUUID, that.responseDomainUUID ))
            return false;
        if (!Objects.equals( responseDomainRevision, that.responseDomainRevision ))
            return false;
        if (!Objects.equals( question, that.question )) return false;
        if (!Objects.equals( intent, that.intent )) return false;
        return Objects.equals( conceptRefs, that.conceptRefs );
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responseDomainUUID != null ? responseDomainUUID.hashCode() : 0);
        result = 31 * result + (responseDomainRevision != null ? responseDomainRevision.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (intent != null ? intent.hashCode() : 0);
        result = 31 * result + (conceptRefs != null ? conceptRefs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QuestionItem{" + super.toString() +
                ", question='" + question + '\'' +
                ", responseDomain=" +  (responseDomain != null ? responseDomain.getName(): "?")  + '\'' +
                ", responseUUID=" + responseDomainUUID + '\'' +
                ", responseRev" + responseDomainRevision  + '\'' +
                "} " + System.lineSeparator();
    }

    @Override
    public QuestionItemFragmentBuilder getXmlBuilder() {
        return new QuestionItemFragmentBuilder(this);
    }



    @Override
    public void fillDoc(PdfReport pdfReport,String counter)  {
        pdfReport.addHeader(this,"QuestionItem");
        pdfReport.addParagraph(this.getQuestion());
        if(!StringTool.IsNullOrTrimEmpty(getIntent())) {
            pdfReport.addheader2("Intent")
            .add(new Paragraph(this.getIntent()));
        }
        if (getResponseDomain() != null) {
            this.getResponseDomain().fillDoc( pdfReport, "" );
        }
        if(getComments().size()>0)
            pdfReport.addheader2("Comments");
        pdfReport.addComments(getComments());
    }

    @Override
    protected void beforeUpdate() { }

    @Override
    protected void beforeInsert() { }

}

