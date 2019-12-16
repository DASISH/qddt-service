package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.ConditionConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.questionitem.QuestionItem;

import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConstructConditionJsonView extends ConstructJsonView {

    private String condition;


    public ConstructConditionJsonView(ConditionConstruct construct) {
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
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals( o )) return false;

        ConstructConditionJsonView that = (ConstructConditionJsonView) o;

        return condition != null ? condition.equals( that.condition ) : that.condition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner( ", ", ConstructConditionJsonView.class.getSimpleName() + "[", "]" )
            .add( "condition='" + condition + "'" )
            .add( "name='" + getName() + "'" )
            .add( "id=" + getId() )
            .add( "classKind='" + getClassKind() + "'" )
            .toString();
    }


}
