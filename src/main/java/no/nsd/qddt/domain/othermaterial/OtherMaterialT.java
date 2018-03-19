package no.nsd.qddt.domain.othermaterial;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
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
@Entity
@Audited
@DiscriminatorValue("T")
public class OtherMaterialT extends OtherMaterial {

    public OtherMaterialT() {
    }

    public OtherMaterialT(UUID parentId, MultipartFile file) {
        super( parentId, file );
    }

    public OtherMaterialT(OtherMaterial om) {
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
    @JsonBackReference(value = "tref")
    @JoinColumn(name = "OWNER_ID",insertable = false, updatable = false)
    private TopicGroup parent;

    public TopicGroup getParent() {
        return parent;
    }

    public void setParent(TopicGroup parent) {
        this.parent = parent;
        this.setOwnerId( parent.getId() );
    }

    @Override
    public OtherMaterialT clone() {
        return (OtherMaterialT)super.clone();
}
}