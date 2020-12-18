package no.nsd.qddt.domain.referenced;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "referenced")
@Immutable
public class Referenced {
    @Id
    public UUID entity;
    public String kind;
    public Timestamp modified;
    public Long antall;

    // @Type(type = "no.nsd.qddt.utils.GenericArrayType")
    // public UUID[] refs;

    public UUID getEntity() {
        return entity;
    }

    public void setEntity(UUID entity) {
        this.entity = entity;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public Long getAntall() {
        return antall;
    }

    public void setAntall(Long antall) {
        this.antall = antall;
    }

    // public UUID[] getRefs() {
    //     return refs;
    // }

    // public void setRefs(UUID[] refs) {
    //     this.refs = refs;
    // }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Referenced that = (Referenced) o;

        if (entity != null ? !entity.equals( that.entity ) : that.entity != null) return false;
        if (kind != null ? !kind.equals( that.kind ) : that.kind != null) return false;
        return antall != null ? antall.equals( that.antall ) : that.antall == null;
    }

    @Override
    public int hashCode() {
        int result = entity != null ? entity.hashCode() : 0;
        result = 31 * result + (kind != null ? kind.hashCode() : 0);
        result = 31 * result + (antall != null ? antall.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner( ", ", Referenced.class.getSimpleName() + "[", "]" )
            .add( "entity=" + entity )
            .add( "kind='" + kind + "'" )
            .add( "antall=" + antall )
            .toString();
    }


}
