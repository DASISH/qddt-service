package no.nsd.qddt.domain.questionItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
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
public class QuestionItem extends AbstractEntityAudit {

    private String question;
    private String intent;
    /**
     * This field will be populated with the correct version of a RD,  but should never be persisted.
     */
    private ResponseDomain responseDomain;
    /**
     * This field must be available "raw" in order to set and query  responsedomain by ID
     */
    private UUID responseDomainUUID;
    private Long responseDomainRevision;

    private  Set<ConceptRef> conceptRefs;

    public QuestionItem() {
        //conceptRefs = new ArrayList<>( 0 );
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsedomain_id",insertable = false,updatable = false)
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
            LOG.info("MISSING ManagedRepresentation "+ responseDomain.getManagedRepresentation());
        }
            this.responseDomain = responseDomain;
        if (this.responseDomain != null)
            this.responseDomain.getVersion().setRevision(this.responseDomainRevision);
    }


    @Column(name = "responsedomain_revision")
    public Long getResponseDomainRevision() {
        return responseDomainRevision == null? 0:responseDomainRevision;
    }
    public void setResponseDomainRevision(Long responseDomainRevision) {
        this.responseDomainRevision = responseDomainRevision;
    }


    @JsonIgnore
    @Type(type="pg-uuid")
    @Column(name="responsedomain_id")
    public UUID getResponseDomainUUID() {
        return responseDomainUUID;
    }
    public void setResponseDomainUUID(UUID responseDomainUUID) {
        this.responseDomainUUID = responseDomainUUID;
    }



    @Column(name = "question")
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }


    @Column(name = "intent")
    public String getIntent() {
        return intent;
    }
    public void setIntent(String intent) {
        this.intent = intent;
    }

    @Transient
    @JsonSerialize
    public Set<ConceptRef> getConceptRefs(){
        return conceptRefs;
    }
    @JsonDeserialize
    public void setConceptRefs(Set<ConceptRef> conceptRefs) {
        this.conceptRefs = conceptRefs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionItem)) return false;
        if (!super.equals( o )) return false;

        QuestionItem that = (QuestionItem) o;

        if (question != null ? !question.equals( that.question ) : that.question != null) return false;
        if (intent != null ? !intent.equals( that.intent ) : that.intent != null) return false;
        if (responseDomainUUID != null ? !responseDomainUUID.equals( that.responseDomainUUID ) : that.responseDomainUUID != null)
            return false;
        return responseDomainRevision != null ? responseDomainRevision.equals( that.responseDomainRevision ) : that.responseDomainRevision == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (intent != null ? intent.hashCode() : 0);
        result = 31 * result + (responseDomainUUID != null ? responseDomainUUID.hashCode() : 0);
        result = 31 * result + (responseDomainRevision != null ? responseDomainRevision.hashCode() : 0);
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

    // Start pre remove ----------------------------------------------

    @PreRemove
    private void removeReferencesFromQi(){
        setResponseDomain(null);
    }

    public void updateStatusQI() {
        this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        this.setChangeComment("Concept reference removed");
    }

    // End pre remove ----------------------------------------------

}


