package no.nsd.qddt.domain.instrument.pojo;

import no.nsd.qddt.domain.controlconstruct.pojo.ConditionKind;
import no.nsd.qddt.domain.interfaces.IConditionNode;
import no.nsd.qddt.domain.interfaces.IWebMenuPreview;
import no.nsd.qddt.domain.interfaces.Version;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ConditionNode implements IConditionNode, IWebMenuPreview {
    UUID id;
    String name;
    ConditionKind conditionKind;
    String classKind;
    String condition;

    public ConditionNode() {
    }

    public ConditionNode(IConditionNode instance) {
        setId( instance.getId() );
        setName( instance.getName() );
        setClassKind( instance.getClassKind() );
        setConditionKind( instance.getConditionKind());
        setCondition( instance.getCondition() );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public Version getVersion() {
        return null;
    }

    @Override
    public ConditionKind getConditionKind() {
        return conditionKind;
    }

    public void setConditionKind(ConditionKind conditionKind) {
        this.conditionKind = conditionKind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getClassKind() {
        return classKind;
    }

    public void setClassKind(String classKind) {
        this.classKind = classKind;
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

        ConditionNode that = (ConditionNode) o;

        if (id != null ? !id.equals( that.id ) : that.id != null) return false;
        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        if (conditionKind != null ? !conditionKind.equals( that.conditionKind ) : that.conditionKind != null)
            return false;
        if (classKind != null ? !classKind.equals( that.classKind ) : that.classKind != null) return false;
        return condition != null ? condition.equals( that.condition ) : that.condition == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (conditionKind != null ? conditionKind.hashCode() : 0);
        result = 31 * result + (classKind != null ? classKind.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"ConditionNode\", " +
            "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
            "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
            "\"conditionKind\":" + (conditionKind == null ? "null" : "\"" + conditionKind + "\"") + ", " +
            "\"classKind\":" + (classKind == null ? "null" : "\"" + classKind + "\"") + ", " +
            "\"condition\":" + (condition == null ? "null" : "\"" + condition + "\"") +
            "}";
    }


}
