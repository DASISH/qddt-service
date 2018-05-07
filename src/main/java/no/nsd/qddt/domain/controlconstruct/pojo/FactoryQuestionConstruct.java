package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.IEntityFactory;

import java.util.stream.Collectors;

/**
 * @author Stig Norland
 **/

public class FactoryQuestionConstruct implements IEntityFactory<QuestionConstruct> {

    @Override
    public QuestionConstruct create() {
    return new QuestionConstruct();
    }


    @Override
    public QuestionConstruct copyBody(QuestionConstruct source, QuestionConstruct dest) {

        dest.setLabel(source.getLabel());
        dest.setQuestionItem(source.getQuestionItem());
        dest.setQuestionItemUUID(source.getQuestionItemUUID());
        dest.setQuestionItemRevision(source.getQuestionItemRevision());
        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> m.clone()).collect(Collectors.toList()));

        dest.setUniverse(source.getUniverse());
        dest.setControlConstructInstructions(source.getControlConstructInstructions());

    return dest;
    }

}