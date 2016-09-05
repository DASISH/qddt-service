package no.nsd.qddt.domain.downloadtoken;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "downloadtoken")
public class DownloadToken extends AbstractEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "otherMaterial_id")
    private OtherMaterial otherMaterial;

    @Type(type="pg-uuid")
    @Column(name = "uuid")
    private UUID uuid;

    public DownloadToken (OtherMaterial otherMaterial) {
        this.uuid = java.util.UUID.randomUUID();
        this.otherMaterial = otherMaterial;
    }

    public DownloadToken() {
        this.uuid = java.util.UUID.randomUUID();
    }

    public OtherMaterial getotherMaterial() {
        return otherMaterial;
    }

    public void setOtherMaterial(OtherMaterial otherMaterial) {
        this.otherMaterial = otherMaterial;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DownloadToken that = (DownloadToken) o;

        if (otherMaterial != null ? !otherMaterial.equals(that.otherMaterial) : that.otherMaterial != null) return false;
        return !(uuid != null ? !uuid.equals(that.uuid) : that.uuid != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DownloadToken{" +
                ", OtherMaterial=" + otherMaterial +
                ", uuid=" + uuid +
                '}';
    }
}
