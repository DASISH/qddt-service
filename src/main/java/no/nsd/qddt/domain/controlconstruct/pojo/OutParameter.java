package no.nsd.qddt.domain.controlconstruct.pojo;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class OutParameter implements IParameter {
    private String name;
    private UUID referencedId;

    public OutParameter() {
    }

    public OutParameter(String name, UUID referencedId) {
        this.name = name;
        this.referencedId = referencedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getReferencedId() {
        return referencedId;
    }

    public void setReferencedId(UUID referencedId) {
        this.referencedId = referencedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutParameter that = (OutParameter) o;

        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        return referencedId != null ? referencedId.equals( that.referencedId ) : that.referencedId == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (referencedId != null ? referencedId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\"OutParameter\": { " +
            "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
            "\"referencedId\":" + (referencedId == null ? "null" : referencedId) +
            "}";
    }


}
