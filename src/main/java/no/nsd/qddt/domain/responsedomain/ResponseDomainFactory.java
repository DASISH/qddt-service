package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.IEntityFactory;


/**
 * @author Stig Norland
 */

public class ResponseDomainFactory implements IEntityFactory<ResponseDomain> {

	@Override
	public ResponseDomain create() {
		return new ResponseDomain();
	}

	@Override
	public ResponseDomain copyBody(ResponseDomain source, ResponseDomain dest) {
        dest.setName(source.getName());
        dest.setDescription(source.getDescription());
        dest.setDisplayLayout(source.getDisplayLayout());
//        List<Code>  codes = source.getCodes();
        dest.setCodes(source.getCodes() );
        dest.setManagedRepresentation(source.getManagedRepresentation().clone());
				dest.setResponseKind( source.getResponseKind() );
				dest.setResponseCardinality( source.getResponseCardinality() );
        return dest;
	}
    
}
