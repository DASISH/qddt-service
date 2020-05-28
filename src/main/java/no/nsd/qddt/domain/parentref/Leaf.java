package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.embedded.Urn;
import no.nsd.qddt.exception.StackTraceFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Transient;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class Leaf<T extends IRefs> implements IRefsDDI {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Urn urn;
    private String name;
    private IRefs parentRef;

    @Transient
    public T entity;

    public  Leaf(final T entity) {
        assert entity != null;
        try {
            name = entity.getName();
    
            urn.uid = entity.getId();
            version = entity.getVersion();
            agency = entity.getAgency().getName();
            parentRef = entity.getParentRef();
            this.entity = entity;
        } catch (final NullPointerException npe) {
            LOG.error("LeafRef NullPointerException");
        } catch (final Exception ex) {
            LOG.error(ex.getMessage());
            StackTraceFilter.filter(ex.getStackTrace()).stream().map(a -> a.toString()).forEach(LOG::info);
        }
    }

    @Override
    public IUrnDDI getUrn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IRefs getParentRef() {
        return parentRef;
    }

    public T getEntity() {
        return entity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Leaf))
            return false;

        final Leaf baseRef = (Leaf) o;

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
