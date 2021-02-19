package no.nsd.qddt.domain.instrument.pojo;

import no.nsd.qddt.domain.AbstractEntityAudit;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class Parameter implements Comparable<Parameter> {

    @Column(name ="id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private UUID referencedId;
    private String parameterKind;

    public Parameter() {
    }


    public Parameter(String name) {
        this.name = name;
    }

    public Parameter(String name, String parameterKind) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.parameterKind = parameterKind;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getParameterKind() {
        return parameterKind;
    }

    public void setParameterKind(String parameterKind) {
        this.parameterKind = parameterKind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

        Parameter that = (Parameter) o;

        return name != null ? name.equals( that.name ) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"Parameter\", " +
            "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
            "}";
    }

    private static final String PARAM_FORMAT=
        "%3$s<r:OutParameter isIdentifiable=\"true\" scopeOfUniqueness=\"Maintainable\" isArray=\"false\">\n" +
        "%3$s\t<r:URN>urn:ddi:%1$s</r:URN>\n" +
        "%3$s\t<r:Alias>%2$s</r:Alias>\n" +
        "%3$s</r:OutParameter>\n";
    public String toDDIXml(AbstractEntityAudit entity, String tabs) {
        return String.format( PARAM_FORMAT,  entity.getAgency().getName() + ":" + entity.getVersion().toDDIXml() , getName(),tabs);
    }


    @Override
    public int compareTo(Parameter parameter) {
        int i = parameterKind.compareTo( parameter.parameterKind );
        if (i!=0) return i;
        return name.compareTo(parameter.name);
    }
}


