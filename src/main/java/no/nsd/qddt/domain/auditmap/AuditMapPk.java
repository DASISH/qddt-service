package no.nsd.qddt.domain.auditmap;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Embeddable
class AuditMapPk implements Serializable {
    UUID fk;
    private UUID elementId;
    private Integer elementRevision;

    public AuditMapPk(UUID fk, UUID elementId, Integer elementRevision) {
        this.fk = fk;
        this.elementId = elementId;
        this.elementRevision = elementRevision;
    }

    public AuditMapPk() {
    }

    public UUID getFk() {
        return fk;
    }

    public void setFk(UUID fk) {
        this.fk = fk;
    }

    public UUID getElementId() {
        return elementId;
    }

    public void setElementId(UUID elementId) {
        this.elementId = elementId;
    }

    public Integer getElementRevision() {
        return elementRevision;
    }

    public void setElementRevision(Integer elementRevision) {
        this.elementRevision = elementRevision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditMapPk)) return false;

        AuditMapPk that = (AuditMapPk) o;

        if (fk != null ? !fk.equals( that.fk ) : that.fk != null) return false;
        if (elementId != null ? !elementId.equals( that.elementId ) : that.elementId != null) return false;
        return elementRevision != null ? elementRevision.equals( that.elementRevision ) : that.elementRevision == null;
    }

    @Override
    public int hashCode() {
        int result = fk != null ? fk.hashCode() : 0;
        result = 31 * result + (elementId != null ? elementId.hashCode() : 0);
        result = 31 * result + (elementRevision != null ? elementRevision.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"AuditMapPk\", " +
            "\"fk\":" + (fk == null ? "null" : fk) + ", " +
            "\"elementId\":" + (elementId == null ? "null" : elementId) + ", " +
            "\"elementRevision\":" + (elementRevision == null ? "null" : "\"" + elementRevision + "\"") +
            "}";
    }

}
