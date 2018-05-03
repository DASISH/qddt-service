package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.IEntityFactory;

import java.util.stream.Collectors;

public class FactoryStatementConstruct implements IEntityFactory<StatementItem> {

	@Override
	public StatementItem create() {
		return new StatementItem();
	}

    @Override
    public StatementItem copyBody(StatementItem source, StatementItem dest) {
        dest.setLabel(source.getLabel());
        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> m.clone())
            .collect(Collectors.toList()));

        dest.setStatement( source.getStatement() );

        return dest;
    }


}