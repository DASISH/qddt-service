package no.nsd.qddt.domain.controlconstruct.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterialConstruct;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
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

    @OneToMany(mappedBy = "parent" ,fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
//    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    private Set<OtherMaterialConstruct> otherMaterials = new HashSet<>();



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


    public Set<OtherMaterialConstruct> getOtherMaterials() {
        return otherMaterials;
    }
    public void setOtherMaterials(Set<OtherMaterialConstruct> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }
    public OtherMaterialConstruct addOtherMaterial(OtherMaterialConstruct otherMaterial) {
        otherMaterial.setParent( this );
        return  otherMaterial;
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


}


