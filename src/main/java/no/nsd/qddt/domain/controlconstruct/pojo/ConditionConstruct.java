package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.elementref.ElementRefImpl;
import no.nsd.qddt.domain.interfaces.IConditionNode;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@DiscriminatorValue("CONDITION_CONSTRUCT")
public class ConditionConstruct extends ControlConstruct implements IConditionNode {

    @Column(name = "description")
    private String condition;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONTROL_CONSTRUCT_SUPER_KIND")
    ConditionKind conditionKind;

    @OrderColumn(name="sequence_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONTROL_CONSTRUCT_SEQUENCE",
        joinColumns = @JoinColumn(name="sequence_id", referencedColumnName = "id"))
    private List<ElementRefImpl<ControlConstruct>> sequence = new ArrayList<>(0);

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

    public List<ElementRefImpl<ControlConstruct>> getSequence() {
        return sequence;
    }

    public void setSequence(List<ElementRefImpl<ControlConstruct>> sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConditionConstruct)) return false;
        if (!super.equals( o )) return false;

        ConditionConstruct that = (ConditionConstruct) o;

        if (!Objects.equals( condition, that.condition )) return false;
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

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new ControlConstructFragmentBuilder<>( this );
    }

}
