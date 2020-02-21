package no.nsd.qddt.domain.controlconstruct.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.IElementRef;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Embeddable
public class QuestionItemRef implements IElementRef<QuestionItem> {

    /**
     * This field will be populated with the correct version of a QI,
     * but should never be persisted.
     */
    @JsonSerialize
    @Transient
    private QuestionItem element;

    /**
     * This field must be available "raw" in order to set and query
     * questionitem by ID
     */
    @JsonIgnore
    @Type(type="pg-uuid")
    @Column(name="questionitem_id")
    private UUID elementId;

    @Column(name = "questionitem_revision")
    private Integer elementRevision;

    @Column(name = "question_name", length = 25)
    private String name;

    @Column(name = "question_text", length = 500 )
    private String text;


    @Override
    public ElementKind getElementKind() {
        return ElementKind.QUESTION_ITEM;
    }

    @Override
    public UUID getElementId() {
        return this.elementId;
    }

    @Override
    public Integer getElementRevision() {
        return this.elementRevision;
    }

    @Override
    public void setElementRevision(Integer revisionNumber) {
        this.elementRevision = revisionNumber;
    }
    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name != null) {
            int min = Integer.min( name.length(), 24 );
            this.name = name.substring( 0, min );
        } else {
            this.name = null;
        }
    }

    public void setText(String text) {
        if (text != null) {
            int min = Integer.min( text.length(), 500 );
            this.text = text.substring( 0, min );
        } else {
            this.text = null;
        }
    }

    public String getText() {
        return (this.element != null) ? this.element.getQuestion() : this.text;
    }

    @Override
    public Version getVersion() {
        if (element==null) return null;
        return element.getVersion();
    }

    @Override
    public QuestionItem getElement() {
        return element;
    }

    @Override
    public void setElement(QuestionItem element) {
        this.element = element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionItemRef that = (QuestionItemRef) o;

        if (element != null ? !element.equals( that.element ) : that.element != null) return false;
        if (elementId != null ? !elementId.equals( that.elementId ) : that.elementId != null) return false;
        if (elementRevision != null ? !elementRevision.equals( that.elementRevision ) : that.elementRevision != null)
            return false;
        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        return text != null ? text.equals( that.text ) : that.text == null;
    }

    @Override
    public int hashCode() {
        int result = element != null ? element.hashCode() : 0;
        result = 31 * result + (elementId != null ? elementId.hashCode() : 0);
        result = 31 * result + (elementRevision != null ? elementRevision.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
            "\"elementId\":" + (elementId == null ? "null" : elementId) + ", " +
            "\"elementRevision\":" + (elementRevision == null ? "null" : "\"" + elementRevision + "\"") + ", " +
            "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
            "\"elementKind\":" + (getElementKind() == null ? "null" : getElementKind()) +
            "}";
    }


}
