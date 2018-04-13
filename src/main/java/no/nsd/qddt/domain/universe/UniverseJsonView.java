package no.nsd.qddt.domain.universe;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class UniverseJsonView {
    private static final long serialVersionUID = 15049624309583L;

    private UUID id;

    private String name;

    private String description;

    private String classKind;

    private Timestamp modified;

    public UniverseJsonView(Universe universe) {
        setId( universe.getId() );
        setName( universe.getName() );
        setDescription( universe.getDescription() );
        setClassKind( universe.getClassKind() );
        setModified( universe.getModified() );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassKind() {
        return classKind;
    }

    public void setClassKind(String classKind) {
        this.classKind = classKind;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniverseJsonView)) return false;

        UniverseJsonView that = (UniverseJsonView) o;

        if (id != null ? !id.equals( that.id ) : that.id != null) return false;
        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        if (description != null ? !description.equals( that.description ) : that.description != null) return false;
        if (classKind != null ? !classKind.equals( that.classKind ) : that.classKind != null) return false;
        return modified != null ? modified.equals( that.modified ) : that.modified == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (classKind != null ? classKind.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"UniverseJsonView\":{"
            + "\"id\":" + id
            + ", \"name\":\"" + name + "\""
            + ", \"description\":\"" + description + "\""
            + ", \"classKind\":\"" + classKind + "\""
            + ", \"modified\":" + modified
            + "}}";
    }


}
