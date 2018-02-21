package no.nsd.qddt.domain.othermaterial;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import org.hibernate.envers.Audited;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@DiscriminatorValue("CC")
public class OtherMaterialCC extends OtherMaterial {

    private ControlConstruct parent;


    public OtherMaterialCC() {
        super();
    }

    public OtherMaterialCC(UUID parentId, MultipartFile file) {
        super( parentId, file );
    }


    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference(value = "ccref")
    @JoinColumn(name = "OWNER_ID")
    public ControlConstruct getParent() {
        return parent;
    }
    public void setParent(ControlConstruct parent) {
        this.parent = parent;
        //setField("ownerId",parent.getId());
    }
}
