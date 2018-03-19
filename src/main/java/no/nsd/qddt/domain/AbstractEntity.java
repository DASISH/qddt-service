package no.nsd.qddt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Audited
@MappedSuperclass
@EntityListeners(EntityCreatedModifiedDateAuditEventConfiguration.class)
public abstract class AbstractEntity {

    @Transient
    @JsonIgnore
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Id
    @Type(type="pg-uuid")
    @Column(name = "id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;


    @Column(name = "updated")
    @Version()
    private Timestamp modified;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
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

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity)) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (modified != null ? !modified.equals(that.modified) : that.modified != null) return false;
        return modifiedBy != null ? modifiedBy.equals(that.modifiedBy) : that.modifiedBy == null;

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
        return "\"id\": \"" + (id == null ? "null" : id) + "\", " +
                "\"modified\":\"" + (modified == null ? "null" : modified) + "\", " +
                "\"modifiedBy\":" + (modifiedBy == null ? "null" : modifiedBy)+ ", ";

    }

    public String toDDIXml(){
        return "<ID type='ID'>" + getId().toString() + "</ID>";
    }


    /*
    Set Properties without notifying Hibernate, good for circumvent auto HiberFix hell.
     */
    protected void setField(String fieldname, Object value) {
        try {
            Class<?> clazz = getClass();
            Field field = clazz.getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException e) {
            LOG.error("IMPOSSIBLE! ", e );
        } catch (IllegalAccessException e) {
            LOG.error("IMPOSSIBLE! ", e );
        }
    }


}