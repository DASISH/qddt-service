package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.elementref.ElementRef;

import java.util.stream.Collectors;
/**
 * @author Stig Norland
 **/

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

    ConceptFactory factory = new ConceptFactory();
    dest.setChildren(
          source.getChildren().stream()
          .map(concept ->  {
              var copy = factory.copy(concept,dest.getBasedOnRevision());
              copy.setParentC( dest );
              return copy;
          })
          .collect( Collectors.toList())
    );


    dest.setConceptQuestionItems( source.getConceptQuestionItems().stream()
        .map( ElementRef::clone )
        .collect(Collectors.toList()));
    return dest;
	}
    
}