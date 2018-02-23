package no.nsd.qddt.domain.instrument;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.embedded.Version;
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
@Table(name = "INSTRUMENT_CONTROL_CONSTRUCT_REF")
public class ControlConstructRef implements java.io.Serializable {

    private static final long serialVersionUID = -7261887349839337877L;

    private Long id;
    private UUID controlConstructId;
    private Long controlConstructRevision;
    private String name;
    private Version version;

    /*
        -if true, indicate that this node is not independent, but is part of
        an template/childnode of sequence.
     */
    private Boolean isVirtual;
    private ControlConstruct controlConstructInternal;
    private Instrument instrument;

    //------------- Begin Child elements with "enver hack" ----------------------

    private Integer index;
    private ControlConstructRef parentReferenceOnly;
    private List<ControlConstructRef> children = new ArrayList<>();

    //------------- End Child elements with "enver hack"  -----------------------


    public ControlConstructRef() {
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    @Type(type="pg-uuid")
    @Column(name="controlconstruct_id")
    public UUID getControlConstructId() {
        return controlConstructId;
    }
    public void setControlConstructId(UUID controlConstructId) {
        this.controlConstructId = controlConstructId;
    }


    @Column(name = "controlconstruct_revision")
    public Long getControlConstructRevision() {
        return controlConstructRevision;
    }
    public void setControlConstructRevision(Long controlConstructRevision) {
        this.controlConstructRevision = controlConstructRevision;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Embedded
    public Version getVersion() {
        return version;
    }
    public void setVersion(Version version) {
        this.version = version;
    }


    @Column(name = "is_virtual")
    public Boolean isVirtual() {
        return isVirtual;
    }
    public void setVirtual(Boolean virtual) {
        isVirtual = virtual;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "controlconstruct_id",updatable = false,insertable = false)
    public ControlConstruct getControlConstructInternal() {
        return controlConstructInternal;
    }
    public void setControlConstructInternal(ControlConstruct controlConstructInternal) {
        this.controlConstructInternal = controlConstructInternal;
    }


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="instrument_id",updatable = false)
    public Instrument getInstrument() {
        return instrument;
    }
    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }


    //------------- Begin Child elements with "enver hack" ----------------------
    @Column(insertable = false,updatable = false)
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }

    @JsonBackReference(value = "ccrParentRef")
    @ManyToOne()
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    public ControlConstructRef getParentReferenceOnly() {
        return parentReferenceOnly;
    }
    public void setParentReferenceOnly(ControlConstructRef parentReferenceOnly) {
        this.parentReferenceOnly = parentReferenceOnly;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    // Ordered arrayList doesn't work with Enver FIX
    @AuditMappedBy(mappedBy = "parentReferenceOnly", positionMappedBy = "index")
    public List<ControlConstructRef> getChildren() {
        return children;
    }
    public void setChildren(List<ControlConstructRef> children) {
        this.children = children;
    }
    //------------- End Child elements with "enver hack"  -----------------------


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ControlConstructRef)) return false;

        ControlConstructRef that = (ControlConstructRef) o;

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
        if (index != null ? !index.equals(that.index) : that.index != null) return false;
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
        result = 31 * result + (index != null ? index.hashCode() : 0);
        result = 31 * result + (parentReferenceOnly != null ? parentReferenceOnly.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "ControlConstructRef (id=%s, rev=%s, name=%s, version=%s, isVirtual=%s, parent_idx=%s, children=%s)",
                this.controlConstructId, this.controlConstructRevision, this.name, this.version, this.isVirtual,
                this.index, (this.children != null)?this.children.size():0);
    }


}
