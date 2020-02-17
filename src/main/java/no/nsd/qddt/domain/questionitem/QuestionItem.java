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
import no.nsd.qddt.domain.responsedomain.ResponseKind;
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

//    @PreRemove
//    private void removeReferencesFromQi(){
//        setResponseDomain(null);
//    }

    public void updateStatusQI() {
        this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        this.setChangeComment("Concept reference removed");
    }

    // End pre remove ----------------------------------------------


    public ResponseDomain getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomain responseDomain) {
//        CategoryType mrCat = responseDomain.getManagedRepresentation().getCategoryType();
//        if (responseDomain!=null &&
//            (mrCat != CategoryType.BOOLEAN & mrCat != CategoryType.CATEGORY &  mrCat != CategoryType.DATETIME &  mrCat != CategoryType.NUMERIC &  mrCat != CategoryType.TEXT)
//            & responseDomain.getManagedRepresentation().getChildren().isEmpty()){
//            LOG.info("MISSING ManagedRepresentation "+ responseDomain.getManagedRepresentation());
//        }
        if (responseDomain != null) {
            this.responseDomain = responseDomain;
            setResponseDomainName( responseDomain.getName() );
            this.responseDomain.getVersion().setRevision(this.responseDomainRevision);
        } else {
            setResponseDomainName(null);
            setResponseDomainRevision( null );
            setResponseDomainUUID( null );
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
        return "{" +
            "\"id\":" + (getId() == null ? "null" : "\"" + getId() +"\"" ) + ", " +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
            "\"intent\":" + (intent == null ? "null" : "\"" + intent + "\"") + ", " +
            "\"question\":" + (question == null ? "null" : "\"" + question + "\"") + ", " +
            "\"responseDomainName\":" + (responseDomainName == null ? "null" : "\"" + responseDomainName + "\"") + ", " +
            "\"modified\":" + (getModified() == null ? "null" : "\"" + getModified()+ "\"" ) + " , " +
            "\"modifiedBy\":" + (getModifiedBy() == null ? "null" : getModifiedBy()) +
            "}";
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


