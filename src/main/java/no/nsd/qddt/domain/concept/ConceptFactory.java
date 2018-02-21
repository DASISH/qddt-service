package no.nsd.qddt.domain.concept;

import java.util.stream.Collectors;
import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.concept.Concept;

public class ConceptFactory implements IEntityFactory<Concept> {

	@Override
	public Concept create() {
		return new Concept();
	}

	@Override
	public Concept copyBody(Concept source, Concept dest) {
        dest.setDescription(source.getDescription());
        dest.setLabel(source.getLabel());
        dest.setName(source.getName());

        dest.setChildren(source.getChildren().stream()
            .map(mapper -> copy(mapper, dest.getBasedOnRevision()))
            .collect(Collectors.toSet()));

        dest.getChildren().forEach(action -> action.setParentC(dest));

        dest.setConceptQuestionItems( source.getConceptQuestionItems().stream()
            .map(mapper -> new ConceptQuestionItemRev(
                mapper.getQuestionId(), 
                mapper.getQuestionItemRevision()))
            .collect(Collectors.toList()));
        return dest;
	}
    
}