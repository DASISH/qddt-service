package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.IEntityFactory;

import java.util.stream.Collectors;

/**
 * @author Stig Norland
 **/

public class InstrumentFactory implements IEntityFactory<Instrument> {

	@Override
	public Instrument create() {
		return new Instrument();
	}

	@Override
	public Instrument copyBody(Instrument source, Instrument dest) {
        dest.setDescription(source.getDescription());
        dest.setLabel(source.getLabel());
        dest.setName(source.getName());

        dest.setExternalInstrumentLocation( source.getExternalInstrumentLocation() );
        dest.setInstrumentKind( source.getInstrumentKind() );
        dest.setSequence( source.getSequence().stream()
            .map( InstrumentElement::clone ).collect( Collectors.toList() ));

//        dest.setStudy( source.getStudy() );

        return dest;
	}
    
}
