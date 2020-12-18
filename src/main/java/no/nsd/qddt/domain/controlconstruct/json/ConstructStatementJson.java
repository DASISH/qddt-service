package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.StatementItem;

/**
 * @author Stig Norland
 */
public class ConstructStatementJson extends ConstructJson {

    /**
     *
     */
    private static final long serialVersionUID = 1580182686378733470L;
    private final String statement;

    public ConstructStatementJson(StatementItem construct){
        super(construct);
        statement = construct.getStatement();
    }

    public String getStatement() {
        return statement;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstructStatementJson)) return false;
        if (!super.equals( o )) return false;

        ConstructStatementJson that = (ConstructStatementJson) o;

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
