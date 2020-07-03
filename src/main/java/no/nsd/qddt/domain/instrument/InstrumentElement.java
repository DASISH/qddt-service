package no.nsd.qddt.domain.instrument;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.controlconstruct.pojo.AbstractParameter;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.OutParameter;
import no.nsd.qddt.domain.elementref.AbstractElementRef;
import no.nsd.qddt.domain.elementref.ElementRef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "INSTRUMENT_ELEMENT")
public class InstrumentElement extends AbstractElementRef<ControlConstruct> {

    @Id
    @GeneratedValue(generator ="UUID")
    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
    @Column(name ="id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne()
    @JsonBackReference(value = "parentRef")
    @JoinColumn(name="instrument_element_id",insertable = false, updatable = false)
    private InstrumentElement parentReferenceOnly;

    @Column(name = "instrument_element_idx", insertable = false, updatable = false)
    private int instrumentElementIdx;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentReferenceOnly", cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE})
    @OrderColumn(name="instrument_element_idx")
    @AuditMappedBy(mappedBy = "parentReferenceOnly", positionMappedBy = "instrumentElementIdx")
    private List<InstrumentElement> sequence = new ArrayList<>(0);


    @ManyToOne( fetch = FetchType.LAZY)
    @JsonBackReference(value = "instrumentRef")
    @JoinColumn(name="instrument_id",updatable = false)
    private Instrument instrument;

    @OrderColumn(name="instrument_element_idx")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "instrumentElement", cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE})
    @AuditMappedBy(mappedBy = "instrumentElement", positionMappedBy = "instrumentElementIdx")
    private Set<OutParameter>parameter = new HashSet<>(0);

    public InstrumentElement() {
    }

    public InstrumentElement(ElementRef<ControlConstruct> elementRef) {
        super();
        setElement( elementRef.getElement() );
        setParameter( elementRef.getElement().getInParameter() );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public <S extends AbstractParameter>  Set<S> getParameter() {
        return (Set<S>) parameter;
    }

    public <S extends AbstractParameter> void setParameter( Set<S>parameter) {
        this.parameter = (Set<OutParameter>) parameter;
    }

    public InstrumentElement getParentReferenceOnly() {
        return parentReferenceOnly;
    }

    public void setParentReferenceOnly(InstrumentElement parentReferenceOnly) {
        this.parentReferenceOnly = parentReferenceOnly;
    }

    public List<InstrumentElement> getSequence() {
        return sequence;
    }

    public void setSequence(List<InstrumentElement> sequence) {
        this.sequence = sequence;
    }

    public void addSequence(InstrumentElement element)
    {
        getSequence().add(element);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals( o )) return false;

        InstrumentElement that = (InstrumentElement) o;

        if (instrumentElementIdx != that.instrumentElementIdx) return false;
        return parentReferenceOnly != null ? parentReferenceOnly.equals( that.parentReferenceOnly ) : that.parentReferenceOnly == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parentReferenceOnly != null ? parentReferenceOnly.hashCode() : 0);
        result = 31 * result + instrumentElementIdx;
        return result;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"InstrumentElement\", " +
            "\"elementId\":" + (getElementId() == null ? "null" : getElementId()) + ", " +
            "\"elementRevision\":" + (getElementRevision() == null ? "null" : "\"" + getElementRevision() + "\"") + ", " +
            "\"elementKind\":" + (getElementKind() == null ? "null" : getElementKind()) + ", " +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
//            "\"inParameter\":" + (inParameter == null ? "null" : Arrays.toString( inParameter.toArray() )) + ", " +
//            "\"outParameter\":" + (outParameter == null ? "null" : Arrays.toString( outParameter.toArray() )) + ", " +
            "\"parentReferenceOnly\":" + (parentReferenceOnly == null ? "null" : parentReferenceOnly) + ", " +
            "\"version\":" + (getVersion() == null ? "null" : getVersion()) +
            "}";
    }

    @Override
    public InstrumentElement clone() {
        InstrumentElement retval = new InstrumentElement();
        retval.setVersion( getVersion() );
        retval.setName( getName() );
        return  retval;
    }


}
