package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.IEntityFactory;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.concept.ConceptFactory;
import no.nsd.qddt.domain.embedded.ElementRef;
import no.nsd.qddt.domain.othermaterial.OtherMaterialT;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            .map( m -> {
                OtherMaterialT om = m.clone();
                om.setOwnerId(dest.getId());
                return om;
            })
            .collect(Collectors.toSet()) );  

        ConceptFactory cf = new ConceptFactory();
        dest.setConcepts(extracted(source)
            .map(mapper ->  cf.copy(mapper,dest.getBasedOnRevision()))
            .collect(Collectors.toSet()));

      dest.setTopicQuestionItems( source.getTopicQuestionItems().stream()
          .map( ElementRef::clone )
          .collect(Collectors.toList()));

      return dest;
	}

	private Stream<Concept> extracted(TopicGroup source) {
		return source.getConcepts().stream();
	}
    
}