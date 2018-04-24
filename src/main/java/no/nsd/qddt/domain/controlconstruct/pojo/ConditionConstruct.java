package no.nsd.qddt.domain.controlconstruct.pojo;

import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@DiscriminatorValue("CONDITION_CONSTRUCT")
public class ConditionConstruct extends ControlConstruct {

    @Column(name = "description")
    private String condition;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONTROL_CONSTRUCT_SUPER_KIND")
    ConditionKind conditionKind;

    public ConditionConstruct() {
        super();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public ConditionKind getConditionKind() {
        return conditionKind;
    }

    public void setConditionKind(ConditionKind conditionKind) {
        this.conditionKind = conditionKind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConditionConstruct)) return false;
        if (!super.equals( o )) return false;

        ConditionConstruct that = (ConditionConstruct) o;

        if (condition != null ? !condition.equals( that.condition ) : that.condition != null) return false;
        return conditionKind == that.conditionKind;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (conditionKind != null ? conditionKind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"ConditionConstruct\":"
            + super.toString()
            + ", \"condition\":\"" + condition + "\""
            + ", \"conditionKind\":\"" + conditionKind + "\""
            + "}";
    }


}