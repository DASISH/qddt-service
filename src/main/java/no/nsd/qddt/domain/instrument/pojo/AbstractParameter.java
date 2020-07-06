package no.nsd.qddt.domain.instrument.pojo;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.interfaces.IParameter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PARAMETER_KIND")
@Table(name = "INSTRUMENT_PARAMETER")
public class AbstractParameter implements IParameter {

//    @JsonBackReference(value = "parentElementRef")

//    @JoinColumn(name="instrument_element_id")
//    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private InstrumentElement instrumentElement;

//    @Column(name = "parent_idx", insertable = false, updatable = false)
//    private int parentElementIdx;


    @Id
    @GeneratedValue(generator ="UUID")
    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
    @Column(name ="id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private UUID referencedId;

    public AbstractParameter() {
    }


    public AbstractParameter(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

//    public InstrumentElement getInstrumentElement() {
//        return instrumentElement;
//    }
//
//    public void setInstrumentElement(InstrumentElement instrumentElement) {
//        this.instrumentElement = instrumentElement;
//    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getReferencedId() {
        return referencedId;
    }

    public void setReferencedId(UUID referencedId) {
        this.referencedId = referencedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractParameter that = (AbstractParameter) o;

        return name != null ? name.equals( that.name ) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"Parameter\", " +
            "\"id\":" + (id == null ? "null" : id) + ", " +
            "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
            "\"referencedId\":" + (referencedId == null ? "null" : referencedId) +
            "}";
    }

    //        "us.mpc:GI_Age_Cohort:1 " +
    private static String PARAM_FORMAT=
        "%3$s<r:OutParameter isIdentifiable=\"true\" scopeOfUniqueness=\"Maintainable\" isArray=\"false\">\n" +
        "%3$s\t<r:URN>urn:ddi:%1$s</r:URN>\n" +
        "%3$s\t<r:Alias>%2$s</r:Alias>\n" +
        "%3$s</r:OutParameter>\n";
    public String toDDIXml(AbstractEntityAudit entity, String tabs) {
        return String.format( PARAM_FORMAT,  entity.getAgency().getName() + ":" + entity.getVersion().toDDIXml() , getName(),tabs);
    }



}
