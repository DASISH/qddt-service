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
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class BaseJsonEdit {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime modified;

    private UserJson modifiedBy;

    private AgencyJsonView agency;

    @Type(type="pg-uuid")
    private UUID basedOnObject;

    @Embedded
    private Version version;

    @Enumerated(EnumType.STRING)
    private AbstractEntityAudit.ChangeKind changeKind;

    public BaseJsonEdit() {
    }

    public BaseJsonEdit(AbstractEntityAudit entity) {
        if (entity == null) return;
        setAgency(new AgencyJsonView(entity.getAgency()));
        setBasedOnObject(entity.getBasedOnObject());
        setModified(entity.getModified());
        setModifiedBy(new UserJson(entity.getModifiedBy()));
        setVersion(entity.getVersion());
        setChangeKind( entity.getChangeKind());
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public UserJson getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserJson modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public AgencyJsonView getAgency() {
        return agency;
    }

    public void setAgency(AgencyJsonView agency) {
        this.agency = agency;
    }

    public UUID getBasedOnObject() {
        return basedOnObject;
    }

    public void setBasedOnObject(UUID basedOnObject) {
        this.basedOnObject = basedOnObject;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public AbstractEntityAudit.ChangeKind getChangeKind() {
        return changeKind;
    }

    public void setChangeKind(AbstractEntityAudit.ChangeKind changeKind) {
        this.changeKind = changeKind;
    }
}
