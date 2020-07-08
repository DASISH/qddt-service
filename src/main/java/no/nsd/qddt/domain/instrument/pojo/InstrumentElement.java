//package no.nsd.qddt.domain.instrument.pojo;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
//import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
//import no.nsd.qddt.domain.elementref.AbstractElementRef;
//import no.nsd.qddt.domain.elementref.ElementKind;
//import no.nsd.qddt.domain.elementref.ElementRef;
//import org.hibernate.annotations.GenericGenerator;
//import org.hibernate.envers.AuditMappedBy;
//import org.hibernate.envers.Audited;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
///**
// * @author Stig Norland
// */
//@Audited
//@Entity
//@Table(name = "INSTRUMENT_ELEMENT")
//public class InstrumentElement extends AbstractElementRef<ControlConstruct> {
//
//    @Id
//    @GeneratedValue(generator ="UUID")
//    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
//    @Column(name ="id", updatable = false, nullable = false)
//    private UUID id;
//
//    ////------------------------------------------------------------------
//    /// parent ref
//    ////------------------------------------------------------------------
//    @Column(name = "instrument_idx", insertable = false, updatable = false)
//    private int instrumentIdx;
//
//    @ManyToOne()
//    @JsonBackReference(value = "instrumentRef")
//    @JoinColumn(name="instrument_id", updatable = false)
//    private Instrument instrument;
//
//    ////------------------------------------------------------------------
//    /// childs parent ref
//    ////------------------------------------------------------------------
//
//    @Column(name = "instrument_element_idx", insertable = false, updatable = false)
//    private int instrumentElementIdx;
//
//    @ManyToOne()
//    @JsonBackReference(value = "parentRef")
//    @JoinColumn(name="instrument_element_id")
//    private InstrumentElement parentReferenceOnly;
//
//
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentReferenceOnly", cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE})
//    @OrderColumn(name="instrument_element_idx")
//    @AuditMappedBy(mappedBy = "parentReferenceOnly", positionMappedBy = "instrumentElementIdx")
//    private List<InstrumentElement> sequence = new ArrayList<>(0);
//
//    ////------------------------------------------------------------------
//    /// params
//    ////------------------------------------------------------------------
//
//
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "instrumentElement", cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
////    @OrderColumn(name="parent_idx")
////    @AuditMappedBy(mappedBy = "instrumentElement", positionMappedBy = "parentElementIdx")
//    private List<OutParameter>outParameters = new ArrayList<>(0);
//
//
//    public InstrumentElement() {
//    }
//
//    public InstrumentElement(ElementRef<ControlConstruct> elementRef) {
//        super();
//        setElement( elementRef.getElement() );
//        setOutParameters( new ArrayList<>(elementRef.getElement().getOutParameter()));
//        if( getElement() != null && elementRef.getElementKind() == ElementKind.SEQUENCE_CONSTRUCT) {
//            setSequence(  ((Sequence)getElement()).getSequence()
//                .stream().map( cc -> new InstrumentElement(cc) ).collect( Collectors.toList()) );
//        }
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public List<OutParameter> getOutParameters() {
//        return outParameters;
//    }
//
//    public void setOutParameters(List<OutParameter> outParameters) {
//        this.outParameters = outParameters;
//    }
//
//
//    public Instrument getInstrument() {
//        return instrument;
//    }
//
//    public void setInstrument(Instrument instrument) {
//        this.instrument = instrument;
//    }
//
//    public List<InstrumentElement> getSequence() {
//        return sequence;
//    }
//
//    public void setSequence(List<InstrumentElement> sequence) {
//        this.sequence = sequence;
//    }
//
//    public void addSequence(InstrumentElement element)
//    {
//        getSequence().add(element);
//    }
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals( o )) return false;
//
//        InstrumentElement that = (InstrumentElement) o;
//
//        if (instrumentElementIdx != that.instrumentElementIdx) return false;
//        return parentReferenceOnly != null ? parentReferenceOnly.equals( that.parentReferenceOnly ) : that.parentReferenceOnly == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (parentReferenceOnly != null ? parentReferenceOnly.hashCode() : 0);
//        result = 31 * result + instrumentElementIdx;
//        return result;
//    }
//
//    @Override
//    public String toString() {
//        return "{\"_class\":\"InstrumentElement\", " +
//            "\"elementId\":" + (getElementId() == null ? "null" : getElementId()) + ", " +
//            "\"elementRevision\":" + (getElementRevision() == null ? "null" : "\"" + getElementRevision() + "\"") + ", " +
//            "\"elementKind\":" + (getElementKind() == null ? "null" : getElementKind()) + ", " +
//            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
////            "\"inParameter\":" + (inParameter == null ? "null" : Arrays.toString( inParameter.toArray() )) + ", " +
////            "\"outParameter\":" + (outParameter == null ? "null" : Arrays.toString( outParameter.toArray() )) + ", " +
//            "\"parentReferenceOnly\":" + (parentReferenceOnly == null ? "null" : parentReferenceOnly) + ", " +
//            "\"version\":" + (getVersion() == null ? "null" : getVersion()) +
//            "}";
//    }
//
//    @Override
//    public InstrumentElement clone() {
//        InstrumentElement retval = new InstrumentElement();
//        retval.setVersion( getVersion() );
//        retval.setName( getName() );
//        return  retval;
//    }
//
//
//}
