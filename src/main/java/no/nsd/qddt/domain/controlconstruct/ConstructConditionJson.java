package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.domain.parameter.CCParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 */
public class ConstructConditionJson extends ConstructJson {

    private String condition;

    private List<CCParameter> parameters = new ArrayList<>();

    ConstructConditionJson(ControlConstruct construct) {
        super(construct);
        condition = construct.getCondition();
        parameters = construct.getParameters();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<CCParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<CCParameter> parameters) {
        this.parameters = parameters;
    }
}
