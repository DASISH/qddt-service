package no.nsd.qddt.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.user.UserJson;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private UUID id;
    private String name;
    private LocalDateTime modified;
    private UserJson modifiedBy;
    private AgencyJsonView agency;
    private UUID basedOnObject;
    private Long basedOnRevision;
    private Version version;
    private AbstractEntityAudit.ChangeKind changeKind;

    protected BaseJsonEdit() {
    }

    protected BaseJsonEdit(AbstractEntityAudit entity) {
        if (entity == null){
            LOG.error("Entity is null");
            StackTraceFilter.nsdStack().stream()
                .map(a->a.toString())
                .forEach(LOG::info);
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

    @Type(type="pg-uuid")
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


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    public LocalDateTime getModified() {
        return modified;
    }
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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


    @Type(type="pg-uuid")
    public UUID getBasedOnObject() {
        return basedOnObject;
    }
    private void setBasedOnObject(UUID basedOnObject) {
        this.basedOnObject = basedOnObject;
    }

    public Long getBasedOnRevision() {
        return basedOnRevision;
    }
    public void setBasedOnRevision(Long basedOnRevision) {
        this.basedOnRevision = basedOnRevision;
    }


    @Embedded
    public Version getVersion() {
        return version;
    }
    private void setVersion(Version version) {
        this.version = version;
    }


    @Enumerated(EnumType.STRING)
    public AbstractEntityAudit.ChangeKind getChangeKind() {
        return changeKind;
    }
    private void setChangeKind(AbstractEntityAudit.ChangeKind changeKind) {
        this.changeKind = changeKind;
    }
}
