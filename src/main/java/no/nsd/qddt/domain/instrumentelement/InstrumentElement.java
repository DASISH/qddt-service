package no.nsd.qddt.domain.instrumentelement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.elementref.ElementRef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;

/**
 * @author Stig Norland
 */
@Audited
@Entity
public class InstrumentElement  {

    @Id
    @Type(type="pg-uuid")
    @Column(name = "id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;


    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "instrument_element_id",updatable = false,insertable = false)
    private InstrumentElement parentReferenceOnly;


    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
    @OrderColumn(name="_idx")
    @OrderBy(value = "instrument_element_idx asc")
    @JoinColumn(name = "instrument_element_id")
    @AuditMappedBy(mappedBy = "parentReferenceOnly")
    private List<InstrumentElement> sequences = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INSTRUMENT_ELEMENT_PARAMETER",
        joinColumns = @JoinColumn(name="instrument_element_id", referencedColumnName = "id"))
    private Set<InstrumentParameter> parameters = new HashSet();


    @Embedded
    private ElementRef elementRef;

    public InstrumentElement() {
        super();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<InstrumentElement> getSequences() {
        return sequences;
    }
    public void setSequences(List<InstrumentElement> sequence) {
        this.sequences = sequence;
    }

    public Set<InstrumentParameter> getParameters() {
        return parameters;
    }
    public void setParameters(Set<InstrumentParameter> parameters) {
        this.parameters = parameters;
    }

    public ElementRef getElementRef() {
        return elementRef;
    }
    public void setElementRef(ElementRef elementRef) {
        this.elementRef = elementRef;
    }
}
