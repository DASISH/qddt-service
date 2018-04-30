package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.concept.ConceptFactory;
import no.nsd.qddt.domain.elementref.ElementRef;

import java.util.stream.Collectors;

class TopicGroupFactory implements IEntityFactory<TopicGroup> {

	@Override
	public TopicGroup create() {
		return new TopicGroup();
	}

	@Override
    public TopicGroup copyBody(TopicGroup source, TopicGroup dest) {
	    dest.setAbstractDescription(source.getAbstractDescription());
      dest.setName(source.getName());
      dest.setOtherMaterials(source.getOtherMaterials().stream()
      .map( m -> m.clone())
      .collect(Collectors.toList()) ); 

      ConceptFactory cf = new ConceptFactory();

      dest.setConcepts(source.getConcepts().stream()
          .map(mapper ->  cf.copy(mapper,dest.getBasedOnRevision()))
          .collect(Collectors.toSet()));

      dest.getConcepts().forEach( concept -> concept.setTopicGroup( dest ) );

      dest.setTopicQuestionItems( source.getTopicQuestionItems().stream()
          .map( ElementRef::clone )
          .collect(Collectors.toList()));

      return dest;
	}


}