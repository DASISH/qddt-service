package no.nsd.qddt.domain.classes.elementref;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.classes.interfaces.IElementRef;
import no.nsd.qddt.domain.classes.interfaces.Version;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Embeddable
public class ElementRefQuestionItem implements IElementRef<QuestionItem> {

    /**
     * This field will be populated with the correct version of a QI,
     * but should never be persisted.
     */
    @Transient
    @JsonSerialize
    private QuestionItem element;

    @Type(type="pg-uuid")
    @Column(name="questionitem_id")
    private UUID elementId;

    @Column(name = "questionitem_revision")
    private Integer elementRevision;

    @Column(name = "question_name", length = 25)
    private String name;

    @Column(name = "question_text", length = 500 )
    private String text;

    @Transient
    @JsonSerialize
    @Enumerated(EnumType.STRING)
    private final ElementKind elementKind = ElementKind.QUESTION_ITEM;

    public ElementRefQuestionItem() {
    }

    public ElementRefQuestionItem(QuestionItem questionItem ) {
        setElement( questionItem );
    }

    @Override
    public ElementKind getElementKind() {
        return this.elementKind;
    }

    @Override
    public UUID getElementId() {
        return this.elementId;
    }

    private void setElementId(UUID id) {
        this.elementId = id;
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
        this.element =  element;

        if (element != null) {
            setElementId( element.getId() );
            setName( element.getName() );
            setText( element.getQuestion() );
            this.getVersion().setRevision(this.elementRevision);
        } else {
            setName(null);
            setText( null );
            setElementRevision( null );
            setElementId( null );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementRefQuestionItem that = (ElementRefQuestionItem) o;

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
