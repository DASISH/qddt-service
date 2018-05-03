package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.elementref.ElementRef;

import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */

public class PublicationFactory implements IEntityFactory<Publication> {

    @Override
    public Publication create() {
        return new Publication();
    }

    @Override
    public Publication copyBody(Publication source, Publication dest) {
        dest.setPurpose( source.getPurpose() );
        dest.setStatus( source.getStatus() );
        dest.setPublicationElements(
            source.getPublicationElements().stream().map( ElementRef::clone ).collect( Collectors.toList() ) );

        return dest;
    }
}