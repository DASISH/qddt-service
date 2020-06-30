package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.interfaces.IParameter;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class OutParameter implements IParameter {
    private UUID id;
    private String name;
    private UUID referencedId;

    public OutParameter() {
    }


    public OutParameter(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public OutParameter(UUID id, String name, UUID referencedId) {
        this.id = id;
        this.name = name;
        this.referencedId = referencedId;
    }

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

        OutParameter that = (OutParameter) o;

        return name != null ? name.equals( that.name ) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "\"OutParameter\": { " +
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
