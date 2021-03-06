package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.concept.ConceptFactory;
import no.nsd.qddt.domain.classes.elementref.ElementRefEmbedded;

import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */

class TopicGroupFactory implements IEntityFactory<TopicGroup> {

	@Override
	public TopicGroup create() {
		return new TopicGroup();
	}

	@Override
    public TopicGroup copyBody(TopicGroup source, TopicGroup dest) {
	    dest.setDescription(source.getDescription());
      dest.setName(source.getName());
      dest.setOtherMaterials(source.getOtherMaterials().stream()
      .map( m -> m.clone())
      .collect(Collectors.toList()) ); 

      ConceptFactory cf = new ConceptFactory();

      dest.setConcepts(source.getConcepts().stream()
          .map(mapper ->  cf.copy(mapper,dest.getBasedOnRevision()))
          .collect(Collectors.toList()));

      dest.getConcepts().forEach( concept -> concept.setTopicGroup( dest ) );

      dest.setTopicQuestionItems( source.getTopicQuestionItems().stream()
          .map( ElementRefEmbedded::clone )
          .collect(Collectors.toList()));

      return dest;
	}


}
