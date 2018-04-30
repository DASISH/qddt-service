package no.nsd.qddt.domain.controlconstruct.factory;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.controlconstruct.pojo.ConditionConstruct;

import java.util.stream.Collectors;

public class ConditionConstructFactory implements IEntityFactory<ConditionConstruct> {

	@Override
	public ConditionConstruct create() {
		return new ConditionConstruct();
	}

    @Override
    public ConditionConstruct copyBody(ConditionConstruct source, ConditionConstruct dest) {
        dest.setLabel(source.getLabel());
        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> m.clone().setOrgRef(source.getId()))
            .collect(Collectors.toList())); 

        return dest;
    }


}