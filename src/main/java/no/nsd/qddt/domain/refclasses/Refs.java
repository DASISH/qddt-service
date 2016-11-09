package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.AbstractEntityAudit;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class Refs {
    private String name;
    private UUID id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    Refs(AbstractEntityAudit entity){
        setName(entity.getName());
        setId(entity.getId());
    }

    Refs(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Refs)) return false;

        Refs refs = (Refs) o;

        if (name != null ? !name.equals(refs.name) : refs.name != null) return false;
        return id != null ? id.equals(refs.id) : refs.id == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
