package no.nsd.qddt.domain.controlconstruct.factory;

import no.nsd.qddt.domain.classes.IEntityFactory;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;

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
        dest.setQuestionItemRef( source.getQuestionItemRef() );
        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> m.clone()).collect(Collectors.toList()));

        dest.setUniverse(source.getUniverse());
        dest.setControlConstructInstructions(source.getControlConstructInstructions());

    return dest;
    }

}
