package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.controlconstructparameter.ResponseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 */
public class ConstructConditionJson extends ConstructJson {

    private String condition;

    private List<ResponseReference> parameters = new ArrayList<>();

    public ConstructConditionJson(ControlConstruct construct) {
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

    public List<ResponseReference> getParameters() {
        return parameters;
    }

    public void setParameters(List<ResponseReference> parameters) {
        this.parameters = parameters;
    }
}
