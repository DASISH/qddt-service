package no.nsd.qddt.domain.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.json.UserJson;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@MappedSuperclass
public abstract class AbstractEntity {

    @Transient
    @JsonIgnore
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Id
    @GeneratedValue(generator ="UUID")
    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
    @Column(name ="id", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "updated")
    @Version()
    private Timestamp modified;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private User modifiedBy;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public UserJson getModifiedBy() {
        return  (modifiedBy != null) ? new UserJson(modifiedBy) : null;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (!Objects.equals( id, that.id )) return false;
        if (!Objects.equals( modified, that.modified )) return false;
        return Objects.equals( modifiedBy, that.modifiedBy );
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        result = 31 * result + (modifiedBy != null ? modifiedBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\"id\": \"" + (id == null ? "null" :  "\"" + id + "\"" ) + "\", " +
                "\"modified\":" + (modified == null ? "null" :  "\"" + modified +  "\"") + ", " +
                "\"modifiedBy\":" + (modifiedBy == null ? "null" : modifiedBy)+ ", ";

    }

    @JsonIgnore
    public abstract AbstractXmlBuilder getXmlBuilder();

}
