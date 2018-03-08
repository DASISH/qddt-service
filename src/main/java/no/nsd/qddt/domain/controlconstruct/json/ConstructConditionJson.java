package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;

/**
 * @author Stig Norland
 */
public class ConstructConditionJson extends ConstructJson {

    private String condition;


    public ConstructConditionJson(ControlConstruct construct) {
        super(construct);
        condition = construct.getCondition();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
