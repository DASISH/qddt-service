package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.embedded.Version;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ConstructJsonView  {

    private static final long serialVersionUID = 15049624309583L;

    private UUID id;

    private String name;

    private String label;

    private Version version;

    private String classKind;

    private Timestamp modified;

    private AgencyJsonView agency;

    public ConstructJsonView(ControlConstruct construct){
        id = construct.getId();
        name = construct.getName();
        label = construct.getLabel();
        modified = construct.getModified();
        version = construct.getVersion();
        classKind = construct.getClassKind();
        agency = new AgencyJsonView(construct.getAgency());
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return the modified
     */
    public Timestamp getModified() {
        return modified;
    }

    /**
     * @return the classKind
     */
    public String getClassKind() {
        return classKind;
    }

    /**
     * @return the version
     */
    public Version getVersion() {
        return version;
    }

    /**
     * @return the agency
     */
    public AgencyJsonView getAgency() {
        return agency;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstructJsonView)) return false;

        ConstructJsonView that = (ConstructJsonView) o;

        if (label != null ? !label.equals( that.label ) : that.label != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        return result;
    }

    @Override
    public String
    toString() {
        return "{\"ConstructJson\":"
            + super.toString()
            + ", \"label\":\"" + label + "\""
            + "}";
    }


}
