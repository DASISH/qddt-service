package no.nsd.qddt.domain.instrumentelement;

import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class InstrumentParameter {

    private String name;

    private UUID referencedId;

    public InstrumentParameter(String name, UUID id) {
        setName(name);
        setReferencedId(id);
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
        if (!(o instanceof InstrumentParameter)) return false;

        InstrumentParameter that = (InstrumentParameter) o;

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
        return "{\"ElementParameter\":{"
            + "\"name\":\"" + name + "\""
            + "}}";
    }


}
