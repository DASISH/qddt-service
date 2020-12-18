package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.classes.xml.AbstractXmlBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@DiscriminatorValue("STATEMENT_CONSTRUCT")
public class StatementItem extends ControlConstruct {

    @Column(name = "description")
    private String statement;

    public StatementItem() {
        super();
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatementItem)) return false;
        if (!super.equals( o )) return false;

        StatementItem that = (StatementItem) o;

        return statement != null ? statement.equals( that.statement ) : that.statement == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (statement != null ? statement.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"StatementConstruct\":"
            + super.toString()
            + ", \"statement\":\"" + statement + "\""
            + "}";
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new ControlConstructFragmentBuilder<>( this );
    }

}
