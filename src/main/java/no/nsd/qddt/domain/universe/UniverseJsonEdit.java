package no.nsd.qddt.domain.universe;

import no.nsd.qddt.domain.AbstractJsonEdit;

/**
 * @author Stig Norland
 */
public class UniverseJsonEdit extends AbstractJsonEdit {

    private static final long serialVersionUID = -2195696472853764486L;


    private String description;


    public UniverseJsonEdit() {
    }

    public UniverseJsonEdit(Universe universe) {
        super(universe);
        setDescription(universe.getDescription());
    }


    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

}
