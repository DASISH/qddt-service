package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.AbstractEntityAudit;

import java.util.UUID;

/**
 * @author Stig Norland
 */
abstract class BaseRef implements Refs{

    private String name;
    private UUID id;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }


    BaseRef(AbstractEntityAudit entity){
        assert entity != null;
        setName(entity.getName());
        setId(entity.getId());
    }

    BaseRef(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseRef)) return false;

        BaseRef baseRef = (BaseRef) o;

        if (name != null ? !name.equals(baseRef.name) : baseRef.name != null) return false;
        return id != null ? id.equals(baseRef.id) : baseRef.id == null;

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
