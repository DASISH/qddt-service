package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.AbstractEntityAudit;
import org.hibernate.envers.Audited;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Stig Norland
 */

@Entity
@Audited
@DiscriminatorValue("IN")
public class InParameter extends AbstractParameter {

    public InParameter() {
    }

    public InParameter(String name) {
        super( name );
    }

    @Override
    public String toString() {
        return "{\"_class\":\"InParameter\", " +
            "\"id\":" + (getId() == null ? "null" : getId()) + ", " +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
            "\"referencedId\":" + (getReferencedId() == null ? "null" : getReferencedId()) +
            "}";
    }

    private static String PARAM_FORMAT=
        "%3$s<r:InParameter isIdentifiable=\"true\" scopeOfUniqueness=\"Maintainable\" isArray=\"false\">\n" +
        "%3$s\t<r:URN>urn:ddi:%1$s</r:URN>\n" +
        "%3$s\t<r:Alias>%2$s</r:Alias>\n" +
        "%3$s</r:InParameter>\n";
    public String toDDIXml(AbstractEntityAudit entity, String tabs) {
        return String.format( PARAM_FORMAT,  entity.getAgency().getName() + ":" + getId() + ":" + entity.getVersion().toDDIXml() , getName(),tabs);
    }


}
