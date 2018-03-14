package no.nsd.qddt.domain.instrumentcontrolconstruct;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.instrument.Instrument;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;

/**
 *
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "INSTRUMENT_CONTROL_CONSTRUCT")
public class InstrumentControlConstruct implements java.io.Serializable {

    private static final long serialVersionUID = -7261887349839337877L;

    @Id
    @GeneratedValue
    private Long id;

    @Type(type="pg-uuid")
    @Column(name="controlconstruct_id")
    private UUID controlConstructId;

    @Column(name = "controlconstruct_revision")
    private Integer controlConstructRevision;

    private String name;

    @Embedded
    private Version version;

    /*
        -if true, indicate that this node is not independent, but is part of
        an template/childnode of sequence.
     */
    private Boolean isVirtual;

    @OrderColumn(name="instrument_control_construct_idx")
    @OrderBy("instrument_control_construct_idx ASC")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INSTRUMENT_CONTROL_CONSTRUCT_PARAMETER",joinColumns = @JoinColumn(name="instrument_control_construct_id") )
    private List<InstrumentParameter> parameters = new ArrayList<>();


    @Transient
    @JsonSerialize
    @JsonDeserialize
    private ControlConstruct controlConstructInternal;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="instrument_id",updatable = false)
    private Instrument instrument;

    //------------- Begin Child elements with "enver hack" ----------------------

    // Ordered arrayList doesn't work with Enver FIX
    @Column(insertable = false,updatable = false)
    private Integer parent_idx;

    @JsonBackReference(value = "ccrParentRef")
    @ManyToOne()
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    private InstrumentControlConstruct parentReferenceOnly;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "parent_idx")
    // Ordered arrayList doesn't work with Enver FIX
    @AuditMappedBy(mappedBy = "parentReferenceOnly", positionMappedBy = "parent_idx")
    private List<InstrumentControlConstruct> children = new ArrayList<>();

    //------------- End Child elements with "enver hack"  -----------------------


    public InstrumentControlConstruct() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getControlConstructId() {
        return controlConstructId;
    }

    public void setControlConstructId(UUID controlConstructId) {
        this.controlConstructId = controlConstructId;
    }

    public Integer getControlConstructRevision() {
        return controlConstructRevision;
    }

    public void setControlConstructRevision(Integer controlConstructRevision) {
        this.controlConstructRevision = controlConstructRevision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Boolean getVirtual() {
        return isVirtual;
    }

    public void setVirtual(Boolean virtual) {
        isVirtual = virtual;
    }

    public List<InstrumentParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<InstrumentParameter> parameters) {
        this.parameters = parameters;
    }

    public ControlConstruct getControlConstruct() {
        return controlConstructInternal;
    }

    protected void setControlConstruct(ControlConstruct controlConstructInternal) {
        this.controlConstructInternal = controlConstructInternal;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    private void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }



    public List<InstrumentControlConstruct> getChildren() {
        return children;
    }

    public void setChildren(List<InstrumentControlConstruct> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstrumentControlConstruct)) return false;

        InstrumentControlConstruct that = (InstrumentControlConstruct) o;

        if (controlConstructId != null ? !controlConstructId.equals(that.controlConstructId) : that.controlConstructId != null)
            return false;
        if (controlConstructRevision != null ? !controlConstructRevision.equals(that.controlConstructRevision) : that.controlConstructRevision != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (isVirtual != null ? !isVirtual.equals(that.isVirtual) : that.isVirtual != null) return false;
        if (controlConstructInternal != null ? !controlConstructInternal.equals(that.controlConstructInternal) : that.controlConstructInternal != null)
            return false;
        if (instrument != null ? !instrument.equals(that.instrument) : that.instrument != null) return false;
        if (parent_idx != null ? !parent_idx.equals(that.parent_idx) : that.parent_idx != null) return false;
        if (parentReferenceOnly != null ? !parentReferenceOnly.equals(that.parentReferenceOnly) : that.parentReferenceOnly != null)
            return false;
        return children != null ? children.equals(that.children) : that.children == null;
    }

    @Override
    public int hashCode() {
        int result = controlConstructId != null ? controlConstructId.hashCode() : 0;
        result = 31 * result + (controlConstructRevision != null ? controlConstructRevision.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (isVirtual != null ? isVirtual.hashCode() : 0);
        result = 31 * result + (controlConstructInternal != null ? controlConstructInternal.hashCode() : 0);
        result = 31 * result + (instrument != null ? instrument.hashCode() : 0);
        result = 31 * result + (parent_idx != null ? parent_idx.hashCode() : 0);
        result = 31 * result + (parentReferenceOnly != null ? parentReferenceOnly.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "ControlConstructRef (id=%s, rev=%s, name=%s, version=%s, isVirtual=%s, parent_idx=%s, children=%s)",
                this.controlConstructId, this.controlConstructRevision, this.name, this.version, this.isVirtual,
                this.parent_idx, (this.children != null)?this.children.size():0);
    }


}
