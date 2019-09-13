package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.questionitem2.QuestionItemFragmentBuilder;
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
        "<d:Concept>\n" +
            "\t%1$s"+
            "\t%2$s"+
            "\t%3$s"+
            "\t<r:Name maxLength=\"250\">\n" +
            "\t\t<r:Content xml:lang=\"%8$s\">%4$s</r:Content>\n" +
            "\t</r:Name>\n" +
            "\t<r:Description maxLength=\"10000\">\n" +
            "\t\t<r:Content xml:lang=\"%8$s\" isPlainText=\"false\">%5$s</r:Content>\n" +
            "\t</r:Description>\n" +
            "\t%6$s" +
            "\t%7$s" +
        "</d:Concept>\n";

            private List<ConceptFragmentBuilder> children;
            private List<QuestionItemFragmentBuilder> questions;

            public ConceptFragmentBuilder(Concept concept) {
                super(concept);
                questions = concept.getConceptQuestionItems().stream()
                .map( cqi ->   (QuestionItemFragmentBuilder)cqi.getElement().getXmlBuilder() )
                .collect( Collectors.toList() );

                children = concept.getChildren().stream()
                    .map( c -> (ConceptFragmentBuilder)c.getXmlBuilder() )
                    .collect( Collectors.toList() );
            }
        
        
            @Override
            public void setEntityBody(Map<UUID, String> fragments) {
                fragments.putIfAbsent( entity.getId(), getXmlBody() );
                for(QuestionItemFragmentBuilder question: questions) {
                    question.setEntityBody( fragments );
                }
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
                    questions.stream()
                        .map(q -> q.getEntityRef())
                        .collect( Collectors.joining() ),
                    children.stream()
                        .map(c -> c.getEntityRef())
                        .collect( Collectors.joining()),
                    entity.getXmlLang());
            }
}
