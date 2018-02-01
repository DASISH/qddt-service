package no.nsd.qddt.domain.othermaterial;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import org.hibernate.envers.Audited;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@DiscriminatorValue("CC")
public class OtherMaterialCC extends OtherMaterial {

    @ManyToOne()
    @JsonBackReference(value = "ccref")
    @JoinColumn(name = "OWNER_ID")
    private ControlConstruct parent;

    public ControlConstruct getParent() {
        return parent;
    }

    public void setParent(ControlConstruct parent) {
        this.parent = parent;
    }
}
