package no.nsd.qddt.domain.othermaterial.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
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
public class OtherMaterialCtrlCtor extends OtherMaterial {

    public OtherMaterialCtrlCtor() {
    }

    public OtherMaterialCtrlCtor(UUID parentId, MultipartFile file) {
        super( parentId, file );
    }

    public OtherMaterialCtrlCtor(OtherMaterial om) {
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
    public OtherMaterialCtrlCtor clone() {
        return new OtherMaterialCtrlCtor(super.clone());
}
}