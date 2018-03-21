package no.nsd.qddt.domain.controlconstruct.factory;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.controlconstruct.pojo.ConditionConstruct;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterialCtrlCtor;

import java.util.stream.Collectors;

public class ConditionConstructFactory implements IEntityFactory<ConditionConstruct> {

	@Override
	public ConditionConstruct create() {
		return new ConditionConstruct();
	}

    @Override
    public ConditionConstruct copyBody(ConditionConstruct source, ConditionConstruct dest) {
        dest.setLabel(source.getLabel());
        dest.setCondition( source.getCondition() );
        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> {
                OtherMaterialCtrlCtor om = m.clone();
                om.setOwnerId(dest.getId());
                return om;
            })
            .collect(Collectors.toSet()) );

        return dest;
    }


}