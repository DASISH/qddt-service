package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.classes.interfaces.Version;
import no.nsd.qddt.domain.user.json.UserJson;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ConstructJsonView  {

    private static final long serialVersionUID = 15049624309583L;

    private final UUID id;

    private final String name;

    private String label;

    private final Version version;

    private final String classKind;

    private final Timestamp modified;

    private final UserJson modifiedBy;


    public ConstructJsonView(ControlConstruct construct){
        id = construct.getId();
        name = construct.getName();
        label = construct.getLabel();
        modified = construct.getModified();
        version = construct.getVersion();
        classKind = construct.getClassKind();
        modifiedBy = construct.getModifiedBy();
        // agency = new AgencyJsonView(construct.getAgency());
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
     * @return the modifiedBy
     */
    public UserJson getModifiedBy() {
        return modifiedBy;
    }
    
    // /**
    //  * @return the agency
    //  */
    // public AgencyJsonView getAgency() {
    //     return agency;
    // }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstructJsonView)) return false;

        ConstructJsonView that = (ConstructJsonView) o;

        return label != null ? label.equals( that.label ) : that.label == null;
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
