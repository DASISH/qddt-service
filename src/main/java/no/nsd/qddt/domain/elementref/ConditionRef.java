package no.nsd.qddt.domain.elementref;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.interfaces.IElementRef;
import no.nsd.qddt.domain.interfaces.Version;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Embeddable
public class ConditionRef<T extends ControlConstruct> implements IElementRef<T> {

    /**
     * This field will be populated with the correct version of a QI,
     * but should never be persisted.
     */
    @Transient
    @JsonSerialize
    private T element;

    @Type(type="pg-uuid")
    @Column(name="questionitem_id")
    private UUID elementId;

    @Column(name = "questionitem_revision")
    private Integer elementRevision;

    @Column(name = "question_name", length = 25)
    private String name;

    @Column(name = "question_text", length = 500 )
    private String condition;

    @Transient
    @JsonSerialize
    @Enumerated(EnumType.STRING)
    private ElementKind elementKind = ElementKind.CONDITION_CONSTRUCT;

    public ConditionRef() {
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

    public String getCondition() {
        return  this.condition;
//        return (this.element != null) ? this.element.get() : this.condition;
    }

    public void setCondition(String condition) {
        if (condition != null) {
            int min = Integer.min( condition.length(), 500 );
            this.condition = condition.substring( 0, min );
        } else {
            this.condition = null;
        }
    }

    @Override
    public Version getVersion() {
        if (element==null) return null;
        return element.getVersion();
    }

    @Override
    public T getElement() {
        return element;
    }

    @Override
    public void setElement(T element) {
        this.element =  element;

        if (element != null) {
            setElementId( element.getId() );
            setName( element.getName() );
//            setCondition( element.getCondition() );
            this.getVersion().setRevision(this.elementRevision);
        } else {
            setName(null);
            setCondition( null );
            setElementRevision( null );
            setElementId( null );
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConditionRef that = (ConditionRef) o;

        if (element != null ? !element.equals( that.element ) : that.element != null) return false;
        if (elementId != null ? !elementId.equals( that.elementId ) : that.elementId != null) return false;
        if (elementRevision != null ? !elementRevision.equals( that.elementRevision ) : that.elementRevision != null)
            return false;
        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        return condition != null ? condition.equals( that.condition ) : that.condition == null;
    }

    @Override
    public int hashCode() {
        int result = element != null ? element.hashCode() : 0;
        result = 31 * result + (elementId != null ? elementId.hashCode() : 0);
        result = 31 * result + (elementRevision != null ? elementRevision.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
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
