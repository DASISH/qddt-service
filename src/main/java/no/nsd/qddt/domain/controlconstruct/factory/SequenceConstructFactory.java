package no.nsd.qddt.domain.controlconstruct.factory;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.controlconstruct.pojo.Sequence;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterialConstruct;

import java.util.stream.Collectors;

public class SequenceConstructFactory implements IEntityFactory<Sequence> {

	@Override
	public Sequence create() {
		return new Sequence();
	}

    @Override
    public Sequence copyBody(Sequence source, Sequence dest) {
        dest.setLabel(source.getLabel());
        dest.setOtherMaterials(source.getOtherMaterials().stream()
            .map( m -> {
                OtherMaterialConstruct om = m.clone();
                om.setOwnerId(dest.getId());
                return om;
            })
            .collect(Collectors.toSet()) );
        dest.setDescription(source.getDescription());
        dest.setSequenceKind( source.getSequenceKind() );
        dest.setSequence( source.getSequence().stream()
            .map( ElementRef::clone )
            .collect(Collectors.toList()));

        return dest;
    }


}