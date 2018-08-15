package no.nsd.qddt.domain.search;

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
@Table(name = "uuidpath")
public class QddtUrl {

    @Type(type="pg-uuid")
    @Id
    UUID id;
    String path;
    String name;

    @Column(name = "user_id")
    UUID userId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QddtUrl)) return false;

        QddtUrl qddtUrl = (QddtUrl) o;

        if (id != null ? !id.equals( qddtUrl.id ) : qddtUrl.id != null) return false;
        if (path != null ? !path.equals( qddtUrl.path ) : qddtUrl.path != null) return false;
        if (name != null ? !name.equals( qddtUrl.name ) : qddtUrl.name != null) return false;
        return userId != null ? userId.equals( qddtUrl.userId ) : qddtUrl.userId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"QddtUrl\":{"
            + "\"id\":" + id
            + ", \"path\":\"" + path + "\""
            + ", \"name\":\"" + name + "\""
            + ", \"userId\":" + userId
            + "}}";
    }


}