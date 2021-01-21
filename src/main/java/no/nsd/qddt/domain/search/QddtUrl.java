package no.nsd.qddt.domain.search;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.classes.elementref.ElementKind;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
    @Transient
    @JsonSerialize
//    @JsonDeserialize
    Integer revision;
    String path;
    String name;

    @Column(name = "user_id")
    UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "elementkind")
    ElementKind elementKind;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
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

    public ElementKind getElementKind() {
        return elementKind;
    }

    public void setElementKind(ElementKind elementKind) {
        this.elementKind = elementKind;
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
