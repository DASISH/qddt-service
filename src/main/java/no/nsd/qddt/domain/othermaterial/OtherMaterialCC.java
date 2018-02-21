package no.nsd.qddt.domain.othermaterial;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import org.hibernate.envers.Audited;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@DiscriminatorValue("CC")
public class OtherMaterialCC extends OtherMaterial {

    public OtherMaterialCC(ControlConstruct parent, MultipartFile file) {
        super( parent.getId(), file );
        this.parent = parent;
    }

    public OtherMaterialCC(UUID parentId, MultipartFile file) {
        super( parentId, file );
    }


    @ManyToOne()
    @JsonBackReference(value = "ccref")
    @JoinColumn(name = "OWNER_ID")
    private ControlConstruct parent;

    public ControlConstruct getParent() {
        return parent;
    }

    public void setParent(ControlConstruct parent) {
        this.parent = parent;
        setField("ownerId",parent.getId());
    }

    @Override
    public OtherMaterialCC clone() {
        return (OtherMaterialCC)super.clone();
    }
}
