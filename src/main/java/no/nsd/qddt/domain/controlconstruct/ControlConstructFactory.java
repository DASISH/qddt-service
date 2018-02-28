package no.nsd.qddt.domain.controlconstruct;

import java.util.stream.Collectors;
import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.othermaterial.OtherMaterialCC;

public class ControlConstructFactory implements IEntityFactory<ControlConstruct> {

	@Override
	public ControlConstruct create() {
		return new ControlConstruct();
	}

	@Override
	public ControlConstruct copyBody(ControlConstruct source, ControlConstruct dest) {
        dest.setDescription(source.getDescription());
        dest.setLabel(source.getLabel());
        dest.setName(source.getName());
        dest.setIndexRationale(source.getIndexRationale());
        dest.setCondition(source.getCondition());
        dest.setControlConstructKind(source.getControlConstructKind());
        dest.setSequenceEnum(source.getSequenceEnum());
        dest.setQuestionItem(source.getQuestionItem());
        dest.setQuestionItemRevision(source.getQuestionItemRevision());

        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> {
                OtherMaterialCC om = m.clone();
                om.setOwnerId(dest.getId());
                return om;
            })
        .collect(Collectors.toSet()) );  
        
        dest.setUniverse(source.getUniverse());
        dest.setControlConstructInstructions(source.getControlConstructInstructions());
        dest.setParameters(source.getParameters());

        dest.setChildren(source.getChildren().stream()
            .map(mapper -> copy(mapper, dest.getBasedOnRevision()))
            .collect(Collectors.toList()));
        dest.getChildren().forEach(action -> action.setParent(dest));

        return dest;
	}
    
}