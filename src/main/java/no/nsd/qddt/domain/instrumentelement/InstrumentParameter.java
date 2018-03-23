package no.nsd.qddt.domain.instrumentelement;

import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class InstrumentParameter {

    private String name;

    private String path;

    public InstrumentParameter(String name, String path) {
        setName(name);
        setPath(path);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstrumentParameter)) return false;

        InstrumentParameter that = (InstrumentParameter) o;

        if (name != null ? !name.equals( that.name ) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "{\"ElementParameter\":{"
            + "\"name\":\"" + name + "\""
            + "}}";
    }


}
