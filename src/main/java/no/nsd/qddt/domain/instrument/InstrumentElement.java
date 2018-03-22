package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.elementref.AbstractElementRef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class InstrumentElement extends AbstractElementRef {

    @Type(type="pg-uuid")
    @Column(name = "id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;


//    @OrderColumn(name="instrument_element_idx")
//    @OrderBy("instrument_element_idx ASC")
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "INSTRUMENT_ELEMENT",
//        joinColumns = @JoinColumn(name="instrument_element_id", referencedColumnName = "id"))
//    private List<InstrumentElement> sequence = new ArrayList<>();

/* 
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INSTRUMENT_ELEMENT_PARAMETER",
        joinColumns = @JoinColumn(name="instrument_element_id", referencedColumnName = "id"))
    private Set<InstrumentParameter> parameters = new HashSet(); */


    public InstrumentElement() {
        super();
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }


//    public List<InstrumentElement> getSequence() {
//        return sequence;
//    }
//    public void setSequence(List<InstrumentElement> sequence) {
//        this.sequence = sequence;
//    }

//    public Set<InstrumentParameter> getParameters() {
//        return parameters;
//    }
//    public void setParameters(Set<InstrumentParameter> parameters) {
//        this.parameters = parameters;
//    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstrumentElement)) return false;

        InstrumentElement that = (InstrumentElement) o;

        if (id != null ? !id.equals( that.id ) : that.id != null) return false;
        if (elementKind != that.elementKind) return false;
        if (refId != null ? !refId.equals( that.refId ) : that.refId != null) return false;
        if (revisionNumber != null ? !revisionNumber.equals( that.revisionNumber ) : that.revisionNumber != null)
            return false;
        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        if (getVersion() != null ? !getVersion().equals( that.getVersion() ) : that.getVersion() != null) return false;
        if (element != null ? !element.equals( that.element ) : that.element != null) return false;
//        if (sequence != null ? !sequence.equals( that.sequence ) : that.sequence != null) return false;
//        return parameters != null ? parameters.equals( that.parameters ) : that.parameters == null;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (elementKind != null ? elementKind.hashCode() : 0);
        result = 31 * result + (refId != null ? refId.hashCode() : 0);
        result = 31 * result + (revisionNumber != null ? revisionNumber.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        result = 31 * result + (element != null ? element.hashCode() : 0);
//        result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
//        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"InstrumentElement\":{"
            + "\"id\":" + id
            + ", \"elementKind\":\"" + elementKind + "\""
            + ", \"refId\":" + refId
            + ", \"revisionNumber\":\"" + revisionNumber + "\""
            + ", \"name\":\"" + name + "\""
            + ", \"version\":" + getVersion()
            + ", \"element\":" + element
//            + ", \"sequence\":" + sequence
//            + ", \"parameters\":" + parameters
            + "}}";
    }


}
