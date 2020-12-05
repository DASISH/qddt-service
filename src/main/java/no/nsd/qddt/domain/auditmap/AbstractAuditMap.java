package no.nsd.qddt.domain.auditmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.interfaces.IElementRef;
import no.nsd.qddt.domain.interfaces.IWebMenuPreview;
import no.nsd.qddt.domain.interfaces.Version;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "AUDIT_MAP")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ELEMENT_KIND")
@IdClass(AuditMapPk.class)
public class AbstractAuditMap<T extends IWebMenuPreview> implements IElementRef<T>{

    @Enumerated(EnumType.STRING)
    @Column(name = "ELEMENT_KIND", insertable = false, updatable = false)
    private ElementKind elementKind;

    @Id
    @Type(type="pg-uuid")
    private UUID fk;

    @Id
    @Type(type="pg-uuid")
    private UUID elementId;

    @Id
    @Column(name = "ELEMENT_REVISION")
    private Integer elementRevision;


    @Column(name = "ELEMENT_NAME", length = 500)
    protected String name;

    @Column(name = "ELEMENT_MAJOR")
    private Integer major;
    @Column(name = "ELEMENT_MINOR")
    private Integer minor;
    @Column(name = "ELEMENT_VERSION_LABEL")
    private String versionLabel;


    @Transient
    @JsonSerialize
    @JsonDeserialize()
    protected T element;


    public AbstractAuditMap() {}

    @JsonCreator
    public AbstractAuditMap( @JsonProperty("id")UUID id, @JsonProperty("revisionNumber")Integer rev) {
        setElementId(id);
        setElementRevision(rev);    }

    public UUID getFk() {
        return fk;
    }

    public void setFk(UUID fk) {
        this.fk = fk;
    }

    @Override
    public UUID getElementId() {
        return elementId;
    }

    public void setElementId(UUID elementId) {
        this.elementId = elementId;
    }

    @Override
    public Integer getElementRevision() {
        return elementRevision;
    }

    @Override
    public void setElementRevision(Integer elementRevision) {
        this.elementRevision = elementRevision;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Version getVersion() {
        return new Version(major,minor,elementRevision,versionLabel);
    }

    @Override
    public ElementKind getElementKind() {
        return elementKind;
    }

    public void setVersion(Version version) {
        major = version.getMajor();
        minor = version.getMinor();
        versionLabel = version.getVersionLabel();
    }

    @Override
    public T getElement() {
        return element;
    }

    @Override
    public void setElement(T element) {
        this.element = element;
//        setValues();
    }

    @Override
    public AbstractAuditMap<T> clone() {
        AbstractAuditMap<T> retval = new AbstractAuditMap<>( getElementId(),getElementRevision());
        retval.setVersion( getVersion() );
        retval.setName( getName() );
        return retval;
    }

}
