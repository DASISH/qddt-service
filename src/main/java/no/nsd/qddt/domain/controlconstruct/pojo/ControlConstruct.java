package no.nsd.qddt.domain.controlconstruct.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.instrument.pojo.Parameter;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.classes.pdf.PdfReport;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Instrument is the significant relation.
 * Instrument will be asked for all {@link QuestionItem} instances it has and the
 * metadata in this class will be used as visual condition for each {@link QuestionItem}.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CONTROL_CONSTRUCT_KIND")
@Table(name = "CONTROL_CONSTRUCT")
public class ControlConstruct extends AbstractEntityAudit {

    private String label;

    @JsonIgnore
    @Column(name = "CONTROL_CONSTRUCT_KIND",  insertable=false, updatable = false)
    private String controlConstructKind;

    @OrderColumn(name="owner_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONTROL_CONSTRUCT_OTHER_MATERIAL", joinColumns = {@JoinColumn(name = "owner_id", referencedColumnName = "id")})
    private List<OtherMaterial> otherMaterials = new ArrayList<>();

    @Transient
    @JsonSerialize
    private Set<Parameter> parameterIn = new HashSet<>(0);

    @Transient
    @JsonSerialize
    private Set<Parameter> parameterOut = new HashSet<>(0);


    public ControlConstruct() {
        super();
    }


    @PostLoad
    private void setDefault(){
        setClassKind( controlConstructKind );
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }


    public List<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }
    public void setOtherMaterials(List<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }


    public Set<Parameter> getParameterIn() {
        return parameterIn;
    }

    public void setParameterIn(Set<Parameter> parameterIn) {
        this.parameterIn = parameterIn;
    }

    public Set<Parameter> getParameterOut() {
        return parameterOut;
    }

    public void setParameterOut(Set<Parameter> parameterOut) {
        this.parameterOut = parameterOut;
    }

    @Override
    public void fillDoc(PdfReport pdfReport,String counter)  {  }

    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ControlConstruct)) return false;
        if (!super.equals( o )) return false;

        ControlConstruct that = (ControlConstruct) o;

        if (label != null ? !label.equals( that.label ) : that.label != null) return false;
        return otherMaterials != null ? otherMaterials.equals( that.otherMaterials ) : that.otherMaterials == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (otherMaterials != null ? otherMaterials.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"ControlConstruct\":"
            + super.toString()
            + ", \"label\":\"" + label + "}";
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new ControlConstructFragmentBuilder<>(this);
    }

}


