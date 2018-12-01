package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.questionitem.QuestionItemFragmentBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConceptFragmentBuilder extends XmlDDIFragmentBuilder<Concept> {

    private final String xmlConcept =
        "<d:TopicGroup>\n" +
            "\t%1$s\n" +
            "\t%2$s\n" +
            "\t%3$s\n" +
            "\t<r:Name maxLength=\"250\">\n" +
            "\t\t<r:Content xml:lang=\"eng-GB\" isTranslated=\"false\" isTranslatable=\"true\" isPlainText=\"true\">%4$s</r:Content>\n" +
            "\t</r:Name>\n" +
            "\t<r:Description>\n" +
            "\t\t<r:Content xml:lang=\"eng-GB\" isTranslated=\"false\" isTranslatable=\"true\" isPlainText=\"true\">%5$s</r:Content>\n" +
            "\t</r:Description>\n" +
            "\t%6$s\n" +
            "</d:TopicGroup>";

            private List<ConceptFragmentBuilder> children;
            private List<QuestionItemFragmentBuilder> questions;

            public ConceptFragmentBuilder(Concept concept) {
                super(concept);
                questions = concept.getConceptQuestionItems().stream()
                .map( cqi -> cqi.getElement().getXmlBuilder() )
                .collect( Collectors.toList() );

                children = concept.getChildren().stream()
                    .map( c -> (ConceptFragmentBuilder)c.getXmlBuilder() )
                    .collect( Collectors.toList() );
            }
        
        
            @Override
            public void setEntityBody(Map<UUID, String> fragments) {
                fragments.putIfAbsent( entity.getId(), getXmlBody() );
                for(ConceptFragmentBuilder child : children) {
                    child.setEntityBody( fragments );
                }
            }
        
            private String getXmlBody() {
                return String.format( xmlConcept,
                    getId(),
                    getRationale(),
                    getBasedOn(),
                    entity.getName(),
                    entity.getDescription(),
                    children.stream()
                        .map(c -> c.getEntityRef())
                        .collect( Collectors.joining("\n")) );
            }
}
