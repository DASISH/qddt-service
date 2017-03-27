package no.nsd.qddt.domain.controlconstruct;

/**
 * @author Stig Norland
 */
public class ConstructStatementJson extends ConstructJson {

    private String description;


    ConstructStatementJson(ControlConstruct construct){
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
