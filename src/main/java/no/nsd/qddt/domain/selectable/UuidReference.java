package no.nsd.qddt.domain.selectable;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Entity
@Immutable
@Table(name = "UUID_REFERENCE")
public class UuidReference {

    @Id
    @Column(name = "id")
    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private String tableName;


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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UuidReference)) return false;

        UuidReference that = (UuidReference) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return tableName != null ? tableName.equals(that.tableName) : that.tableName == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidReference{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
