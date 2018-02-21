package no.nsd.qddt.domain.responsedomain;

import java.util.List;
import no.nsd.qddt.domain.IEntityFactory;

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
        List<Code>  codes = source.getCodes();
        dest.setManagedRepresentation(source.getManagedRepresentation().clone());

        return dest;
	}
    
}