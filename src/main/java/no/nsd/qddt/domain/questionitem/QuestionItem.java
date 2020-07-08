package no.nsd.qddt.domain.questionitem;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.elementref.ResponseDomainRef;
import no.nsd.qddt.domain.elementref.ParentRef;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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


    @Embedded
    private ResponseDomainRef responseDomainRef;

    @Column(name = "question", length = 2000)
    private String question;

    @Column(name = "intent", length = 3000)
    private String intent;


    @Transient
    @JsonSerialize
    private List<ParentRef<?>> parentRefs = new ArrayList<>( 0 );


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

    public ResponseDomainRef getResponseDomainRef() {

        return (responseDomainRef==null) ? new ResponseDomainRef(): responseDomainRef;
    }

    public void setResponseDomainRef(ResponseDomainRef responseDomainRef) {
        this.responseDomainRef = responseDomainRef;
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


    public List<ParentRef<?>> getParentRefs() {
        return parentRefs;
    }

    public void setParentRefs(List<ParentRef<?>> parentRefs) {
        this.parentRefs = parentRefs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals( o )) return false;

        QuestionItem that = (QuestionItem) o;

        if (responseDomainRef != null ? !responseDomainRef.equals( that.responseDomainRef ) : that.responseDomainRef != null)
            return false;
        if (question != null ? !question.equals( that.question ) : that.question != null) return false;
        if (intent != null ? !intent.equals( that.intent ) : that.intent != null) return false;
        return parentRefs != null ? parentRefs.equals( that.parentRefs ) : that.parentRefs == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responseDomainRef != null ? responseDomainRef.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (intent != null ? intent.hashCode() : 0);
        result = 31 * result + (parentRefs != null ? parentRefs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
            "\"id\":" + (getId() == null ? "null" : "\"" + getId() +"\"" ) + ", " +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
            "\"intent\":" + (intent == null ? "null" : "\"" + intent + "\"") + ", " +
            "\"question\":" + (question == null ? "null" : "\"" + question + "\"") + ", " +
            "\"responseDomainName\":" + (responseDomainRef.getName() == null ? "null" : "\"" + responseDomainRef.getName() + "\"") + ", " +
            "\"modified\":" + (getModified() == null ? "null" : "\"" + getModified()+ "\"" ) + " , " +
            "\"modifiedBy\":" + (getModifiedBy() == null ? "null" : getModifiedBy()) +
            "}";
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
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
        if (getResponseDomainRef().getElement() != null) {
            this.getResponseDomainRef().getElement().fillDoc( pdfReport, "" );
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


