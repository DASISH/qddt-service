package no.nsd.qddt.domain.controlconstruct.jsonconverter;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;

/**
 * @author Stig Norland
 */
public class ConstructStatementJson extends ConstructJson {

    private String description;


    public ConstructStatementJson(ControlConstruct construct){
        super(construct);
        description = construct.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
