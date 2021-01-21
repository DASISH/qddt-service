package no.nsd.qddt.domain.controlconstruct.factory;

import no.nsd.qddt.domain.classes.IEntityFactory;
import no.nsd.qddt.domain.controlconstruct.pojo.ConditionConstruct;

import java.util.stream.Collectors;

/**
 * @author Stig Norland
 **/

public class FactoryConditionConstruct implements IEntityFactory<ConditionConstruct> {

	@Override
	public ConditionConstruct create() {
		return new ConditionConstruct();
	}

    @Override
    public ConditionConstruct copyBody(ConditionConstruct source, ConditionConstruct dest) {
        dest.setLabel(source.getLabel());
        dest.setCondition( source.getCondition() );
        dest.setConditionKind( source.getConditionKind() );
        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> m.clone())
            .collect(Collectors.toList())); 

        return dest;
    }


}
