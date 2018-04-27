package no.nsd.qddt.domain.search;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "uuidpath")
public class QddtUrl {

    @Type(type="pg-uuid")
    @Id
    UUID id;
    String path;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return  path + "/" + id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QddtUrl)) return false;

        QddtUrl qddtUrl = (QddtUrl) o;

        if (id != null ? !id.equals( qddtUrl.id ) : qddtUrl.id != null) return false;
        return path != null ? path.equals( qddtUrl.path ) : qddtUrl.path == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"QddtUrl\":{"
            + "\"id\":" + id
            + ", \"path\":\"" + path + "\""
            + "}}";
    }


}
