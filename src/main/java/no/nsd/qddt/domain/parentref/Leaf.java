package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Transient;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class Leaf<T extends  IParentRef> implements IRefs {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private UUID id;
    private Version version;
    private String agency;

    private String name;
    private IRefs parentRef;

    @Transient
    public T entity;

    public Leaf(T entity){
        assert entity != null;
        try {
            name = entity.getName();
            id = entity.getId();
            version = entity.getVersion();
            agency = entity.getAgency().getName();
            parentRef = entity.getParentRef();
            this.entity = entity;
        } catch (NullPointerException npe){
            LOG.error("LeafRef NullPointerException");
        } catch (Exception ex){
            LOG.error(ex.getMessage());
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
    }


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public String getAgency() {
        return agency;
    }

    @Override
    public IRefs getParentRef() {
        return parentRef;
    }

    public T getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leaf)) return false;

        Leaf baseRef = (Leaf) o;

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
        return "Ref {\n" +
        "   id=" + id + ",\n" +
        "   name='" + name + "',\n" +
        "   parent=" + getParentRef() +
        "}\n";
    }

}
