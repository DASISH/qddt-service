package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.interfaces.IDomainObjectParentRef;
import no.nsd.qddt.domain.interfaces.IParentRef;
import no.nsd.qddt.domain.interfaces.Version;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Transient;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ParentRef<T extends IDomainObjectParentRef> implements IParentRef {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private UUID id;
    private Version version;
    private String name;

    private IParentRef parentRef;

    private String agency;

    @Transient
    public T entity;

    public ParentRef(final T entity) {
        if (entity == null) return;
        try {
            id = entity.getId();
            version = entity.getVersion();
            name = entity.getName();
            agency = entity.getAgency().getName();
            parentRef = entity.getParentRef();
            this.entity = entity;
        } catch (final NullPointerException npe) {
            LOG.error(StackTraceFilter.filter(npe.getStackTrace()).stream().map(a -> a.toString()).findFirst().get());
        } catch (final Exception ex) {
            LOG.error(ex.getMessage());
            StackTraceFilter.filter(ex.getStackTrace()).stream().map(a -> a.toString()).forEach(LOG::info);
        }
    }


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IParentRef getParentRef() {
        return parentRef;
    }


    public String getAgency() {
        return agency;
    }


    public T getEntity() {
        return entity;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentRef(IParentRef parentRef) {
        this.parentRef = parentRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParentRef<?> parentRef = (ParentRef<?>) o;

        if (id != null ? !id.equals( parentRef.id ) : parentRef.id != null) return false;
        if (version != null ? !version.equals( parentRef.version ) : parentRef.version != null) return false;
        if (agency != null ? !agency.equals( parentRef.agency ) : parentRef.agency != null) return false;
        return name != null ? name.equals( parentRef.name ) : parentRef.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (agency != null ? agency.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
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
