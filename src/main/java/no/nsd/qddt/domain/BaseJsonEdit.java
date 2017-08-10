package no.nsd.qddt.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.user.UserJson;
import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class BaseJsonEdit implements Serializable {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime modified;

    private UserJson modifiedBy;

    private AgencyJsonView agency;

    @Type(type="pg-uuid")
    private UUID basedOnObject;

    private Integer basedOnRevision;

    @Embedded
    private Version version;

    @Enumerated(EnumType.STRING)
    private AbstractEntityAudit.ChangeKind changeKind;

    protected BaseJsonEdit() {
    }

    protected BaseJsonEdit(AbstractEntityAudit entity) {
        if (entity == null){
            System.out.println("BaseJsonEdit entity is null");
            StackTraceElement[] stack =  Thread.currentThread().getStackTrace();
            for (int i = 1; i < 6; i++) {
                System.out.println(stack[i]);
            }
            return;
        }
        setId(entity.getId());
        setName(entity.getName());
        setAgency(new AgencyJsonView(entity.getAgency()));
        setBasedOnObject(entity.getBasedOnObject());
        setBasedOnRevision(entity.getBasedOnRevision());
        setModified(entity.getModified());
        setModifiedBy(new UserJson(entity.getModifiedBy()));
        setVersion(entity.getVersion());
        setChangeKind( entity.getChangeKind());
    }

    public UUID getId() {
        return id;
    }

    protected void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    private void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public UserJson getModifiedBy() {
        return modifiedBy;
    }

    protected void setModifiedBy(UserJson modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public AgencyJsonView getAgency() {
        return agency;
    }

    protected void setAgency(AgencyJsonView agency) {
        this.agency = agency;
    }

    public UUID getBasedOnObject() {
        return basedOnObject;
    }

    private void setBasedOnObject(UUID basedOnObject) {
        this.basedOnObject = basedOnObject;
    }

    public Integer getBasedOnRevision() {
        return basedOnRevision;
    }

    public void setBasedOnRevision(Integer basedOnRevision) {
        this.basedOnRevision = basedOnRevision;
    }

    public Version getVersion() {
        return version;
    }

    private void setVersion(Version version) {
        this.version = version;
    }

    public AbstractEntityAudit.ChangeKind getChangeKind() {
        return changeKind;
    }

    private void setChangeKind(AbstractEntityAudit.ChangeKind changeKind) {
        this.changeKind = changeKind;
    }
}
