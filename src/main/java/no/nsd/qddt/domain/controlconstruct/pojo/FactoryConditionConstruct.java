package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.IEntityFactory;

import java.util.stream.Collectors;

public class FactoryConditionConstruct implements IEntityFactory<ConditionConstruct> {

	@Override
	public ConditionConstruct create() {
		return new ConditionConstruct();
	}

    @Override
    public ConditionConstruct copyBody(ConditionConstruct source, ConditionConstruct dest) {
        dest.setLabel(source.getLabel());
        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> m.clone())
            .collect(Collectors.toList())); 

        return dest;
    }


}