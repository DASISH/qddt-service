package no.nsd.qddt.domain.controlconstruct.factory;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterialConstruct;

import java.util.stream.Collectors;

public class QuestionConstructFactory implements IEntityFactory<QuestionConstruct> {

	@Override
	public QuestionConstruct create() {
		return new QuestionConstruct();
	}

    @Override
    public QuestionConstruct copyBody(QuestionConstruct source, QuestionConstruct dest) {
        dest.setLabel(source.getLabel());
        dest.setQuestionItem(source.getQuestionItem());
        dest.setQuestionItemRevision(source.getQuestionItemRevision());

        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> {
                OtherMaterialConstruct om = m.clone();
                om.setOwnerId(dest.getId());
                return om;
            })
            .collect(Collectors.toSet()) );

        dest.setUniverse(source.getUniverse());
        dest.setControlConstructInstructions(source.getControlConstructInstructions());


        return dest;
    }


}