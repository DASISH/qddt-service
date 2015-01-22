package no.nsd.qddt.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "guid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    //ID part of the URN
    private UUID guid;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id")
    // Owner/Agency part of the URN
    private User createdBy;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "change_id")
    private ChangeReason changeReason;

    private String changeComment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public UUID getGuid() { return guid;}

    public void setGuid(UUID guid) {this.guid = guid;}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChangeReason getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(ChangeReason changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangeComment() {
        return changeComment;
    }

    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractEntity)) {
            return false;
        }
        AbstractEntity that = (AbstractEntity) obj;
        return ObjectUtils.nullSafeEquals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (guid != null ? guid.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (changeReason != null ? changeReason.hashCode() : 0);
        result = 31 * result + (changeComment != null ? changeComment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  " ,id=" + id +
                ", guid=" + guid +
                ", created=" + created +
                ", createdBy=" + createdBy +
                ", name='" + name + '\'' +
                ", changeReason=" + changeReason +
                ", changeComment='" + changeComment + '\'';
    }
}
