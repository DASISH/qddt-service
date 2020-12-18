package no.nsd.qddt.domain.controlconstruct.factory;

import no.nsd.qddt.classes.IEntityFactory;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.classes.elementref.ElementRefEmbedded;

import java.util.stream.Collectors;

/**
 * @author Stig Norland
 **/

public class FactorySequenceConstruct implements IEntityFactory<Sequence> {

	@Override
	public Sequence create() {
		return new Sequence();
	}

    @Override
    public Sequence copyBody(Sequence source, Sequence dest) {
        dest.setLabel(source.getLabel());
        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> m.clone())
            .collect(Collectors.toList()));

        dest.setDescription(source.getDescription());
        dest.setSequenceKind( source.getSequenceKind() );
        dest.setSequence( source.getSequence().stream()
            .map( ElementRefEmbedded::clone )
            .collect(Collectors.toList()));

        return dest;
    }


}
