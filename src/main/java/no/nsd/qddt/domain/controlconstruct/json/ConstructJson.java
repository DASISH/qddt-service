package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;

import java.util.List;

/**
 * @author Stig Norland
 */
public class ConstructJson  extends AbstractJsonEdit {

    private String label;

    private List<OtherMaterial> otherMaterials;

    public ConstructJson(ControlConstruct construct){
        super(construct);
        label = construct.getLabel();
        otherMaterials = construct.getOtherMaterials();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstructJson)) return false;

        ConstructJson that = (ConstructJson) o;

        if (label != null ? !label.equals( that.label ) : that.label != null) return false;
        return otherMaterials != null ? otherMaterials.equals( that.otherMaterials ) : that.otherMaterials == null;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (otherMaterials != null ? otherMaterials.hashCode() : 0);
        return result;
    }

    @Override
    public String
    toString() {
        return "{\"ConstructJson\":"
            + super.toString()
            + ", \"label\":\"" + label + "\""
            + ", \"otherMaterials\":" + otherMaterials
            + "}";
    }


}
