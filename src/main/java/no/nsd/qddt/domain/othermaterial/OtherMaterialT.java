package no.nsd.qddt.domain.othermaterial;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.envers.Audited;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@DiscriminatorValue("T")
public class OtherMaterialT extends OtherMaterial {

    @ManyToOne()
    @JsonBackReference(value = "tref")
    @JoinColumn(name = "OWNER_ID")
    private TopicGroup parent;

    public TopicGroup getParent() {
        return parent;
    }

    public void setParent(TopicGroup parent) {
        this.parent = parent;
    }
}