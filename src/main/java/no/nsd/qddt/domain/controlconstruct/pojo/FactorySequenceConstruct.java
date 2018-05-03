package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.elementref.ElementRef;

import java.util.stream.Collectors;

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
            .map( ElementRef::clone )
            .collect(Collectors.toList()));

        return dest;
    }


}