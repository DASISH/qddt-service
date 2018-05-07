package no.nsd.qddt.domain;

import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.user.json.UserJson;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public  abstract class AbstractJsonEdit implements Serializable {


	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Type(type="pg-uuid")
    private UUID id;
    private String name;
    private Timestamp modified;
    private UserJson modifiedBy;
    private AgencyJsonView agency;

    @Type(type="pg-uuid")
    private UUID basedOnObject;
    private Integer basedOnRevision;

    @Embedded
    private Version version;

    @Enumerated(EnumType.STRING)
    private AbstractEntityAudit.ChangeKind changeKind;

    private String classKind;

    protected AbstractJsonEdit() {
    }

    protected AbstractJsonEdit(AbstractEntityAudit entity) {
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
        setClassKind( entity.getClassKind() );
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

    public Timestamp getModified() {
        return modified;
    }

    private void setModified(Timestamp modified) {
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

    public String getClassKind() {
        return classKind;
    }

    public void setClassKind(String classKind) {
        this.classKind = classKind;
    }
}
