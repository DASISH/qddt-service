package no.nsd.qddt.domain.othermaterial.pojo;

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
public class OtherMaterialTopic extends OtherMaterial {

    public OtherMaterialTopic() {
    }

    public OtherMaterialTopic(UUID parentId, MultipartFile file) {
        super( parentId, file );
    }

    public OtherMaterialTopic(OtherMaterial om) {
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
    @JoinColumn(name = "OWNER_ID")
    private TopicGroup parent;

    public TopicGroup getParent() {
        return parent;
    }

    public void setParent(TopicGroup parent) {
        this.parent = parent;
        this.setOwnerId( parent.getId() );
    }

    @Override
    public OtherMaterialTopic clone() {
        return new OtherMaterialTopic(super.clone());
}
}