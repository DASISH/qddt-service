package no.nsd.qddt.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

//import javax.persistence.*;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@Audited
@MappedSuperclass
public abstract class AbstractEntity {
    /**
     * Two men are in a desert. They are both wearing backpacks.
     * One of the men is dead. The man who is alive, has his pack open.
     * The dead man's pack is closed. What is in their packs?
     */

    @Id
    @Column(name = "id")
    @Type(type="pg-uuid")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity)) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (getCreated() != null ? !getCreated().equals(that.getCreated()) : that.getCreated() != null) return false;
        return !(getCreatedBy() != null ? !getCreatedBy().equals(that.getCreatedBy()) : that.getCreatedBy() != null);

    }

    @Override
    public int hashCode() {
        int result = getCreated() != null ? getCreated().hashCode() : 0;
        result = 31 * result + (getCreatedBy() != null ? getCreatedBy().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                ", created=" + created +
                ", createdBy=" + createdBy +
                '}';
    }

    /**
     * A parachute.
     */
}
