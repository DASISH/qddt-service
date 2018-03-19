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
@Entity
@Audited
@DiscriminatorValue("CC")
public class OtherMaterialCC extends OtherMaterial {

    public OtherMaterialCC() {
    }

    public OtherMaterialCC(UUID parentId, MultipartFile file) {
        super( parentId, file );
    }

    public  OtherMaterialCC(OtherMaterial om) {
        setOwnerId( om.getOwnerId() );
        setFileName( om.getFileName() );
        setFileType( om.getFileType() );
        setOriginalName( om.getOriginalName() );
        setSize( om.getSize() );
        setId( om.getId() );
        setOrgRef( om.getOrgRef() );
        setModified( om.getModified() );
        setModifiedBy( om.getModifiedBy() );
    }



    @ManyToOne()
    @JsonBackReference(value = "ccref")
    @JoinColumn(name = "OWNER_ID",insertable = false, updatable = false)
    private ControlConstruct parent;

    public ControlConstruct getParent() {
        return parent;
    }

    public void setParent(ControlConstruct parent) {
        this.parent = parent;
        this.setOwnerId( parent.getId() );
    }

    @Override
    public OtherMaterialCC clone() {
        return new OtherMaterialCC(super.clone());
}
}