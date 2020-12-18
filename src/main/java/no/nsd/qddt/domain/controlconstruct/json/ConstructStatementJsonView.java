package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.StatementItem;

/**
 * @author Stig Norland
 */
public class ConstructStatementJsonView extends ConstructJsonView {

    private final String statement;

    public ConstructStatementJsonView(StatementItem construct){
        super(construct);
        statement = construct.getStatement();
    }


    public String getStatement() {
        return statement;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals( o )) return false;

        ConstructStatementJsonView that = (ConstructStatementJsonView) o;

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
        return "{\"ConstructStatementJson\":"
            + super.toString()
            + ", \"statement\":\"" + statement + "\""
            + "}";
    }

}
