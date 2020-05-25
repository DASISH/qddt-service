package no.nsd.qddt.domain.controlconstruct.json;


import no.nsd.qddt.domain.controlconstruct.pojo.ConditionConstruct;

/**
 * @author Stig Norland
 */
public class ConstructConditionJson extends ConstructJson {

    /**
     *
     */
    private static final long serialVersionUID = 8040983806216492534L;
    private String condition;


    public ConstructConditionJson(ConditionConstruct construct) {
        super(construct);
        condition = construct.getCondition();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstructConditionJson)) return false;

        ConstructConditionJson that = (ConstructConditionJson) o;

        return condition != null ? condition.equals( that.condition ) : that.condition == null;
    }

    @Override
    public int hashCode() {
        return condition != null ? condition.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{\"ConstructConditionJson\":"
            + super.toString()
            + ", \"condition\":\"" + condition + "\""
            + "}";
    }


}
