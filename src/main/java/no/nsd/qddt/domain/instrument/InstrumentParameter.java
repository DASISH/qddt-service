package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.interfaces.IParameter;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class InstrumentParameter implements Cloneable, IParameter {

    private UUID id;

    private String name;

    private UUID referencedId;

    public InstrumentParameter() {}

    public InstrumentParameter(String name, UUID referencedId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.referencedId = referencedId;
    }

    public InstrumentParameter(UUID id, String name, UUID referencedId) {
        this.id = id;
        this.name = name;
        this.referencedId = referencedId;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
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

        InstrumentParameter that = (InstrumentParameter) o;

        if (!id.equals( that.id )) return false;
        if (!name.equals( that.name )) return false;
        return referencedId != null ? referencedId.equals( that.referencedId ) : that.referencedId == null;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "{\"ElementParameter\":{"
            + "\"name\":\"" + name + "\""
            + "}}";
    }

    public InstrumentParameter clone() {
        return new InstrumentParameter( UUID.randomUUID(), name, referencedId );
    }

}
