package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author Stig Norland
 */
abstract class BaseRef<T> implements IRefs<T> {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private String name;
    private UUID id;
    private Version version;
    private String agency;

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

    BaseRef(AbstractEntityAudit entity){
        assert entity != null;
        try {
            name = entity.getName();
            id = entity.getId();
            version = entity.getVersion();
            agency = entity.getAgency().getName();
        } catch (NullPointerException npe){
            LOG.error("BaseRef NullPointerException");
        } catch (Exception ex){
            LOG.error(ex.getMessage());
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
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

    public int compareTo(BaseRef o) {
        if (o==null) return 1;
        return this.getName().compareToIgnoreCase(o.getName());
    }


}
