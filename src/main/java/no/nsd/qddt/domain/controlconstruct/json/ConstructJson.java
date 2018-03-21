package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterialCtrlCtor;

import java.util.Set;

/**
 * @author Stig Norland
 */
public class ConstructJson  extends AbstractJsonEdit {

    private String label;

    private String description;

    private Set<OtherMaterialCtrlCtor> otherMaterials;



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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<OtherMaterialCtrlCtor> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterialCtrlCtor> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstructJson)) return false;

        ConstructJson that = (ConstructJson) o;

        if (label != null ? !label.equals( that.label ) : that.label != null) return false;
        return description != null ? description.equals( that.description ) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"ConstructJson\":"
            + super.toString()
            + ", \"label\":\"" + label + "\""
            + ", \"description\":\"" + description + "\""
            + "}";
    }

}
