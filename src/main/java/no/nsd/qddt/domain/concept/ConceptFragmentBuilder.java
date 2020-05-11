package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConceptFragmentBuilder extends XmlDDIFragmentBuilder<Concept> {

    private final String xmlConcept =
        "\t\t<d:Concept>\n" +
        "\t\t\t%1$s"+
        "%2$s"+
        "%3$s"+
        "\t\t\t<r:Name maxLength=\"250\">\n" +
        "\t\t\t\t<r:Content xml:lang=\"%8$s\">%4$s</r:Content>\n" +
        "\t\t\t</r:Name>\n" +
        "\t\t\t<r:Description maxLength=\"10000\">\n" +
        "\t\t\t\t<r:Content xml:lang=\"%8$s\" isPlainText=\"false\">%5$s</r:Content>\n" +
        "\t\t\t</r:Description>\n" +
        "%6$s" +
        "%7$s" +
        "\t\t</d:Concept>\n";

            private List<AbstractXmlBuilder> children;
            private List<AbstractXmlBuilder> questions;

            public ConceptFragmentBuilder(Concept concept) {
                super(concept);
                // TODO fix questions, add them to export, then reference Concept
                questions = concept.getConceptQuestionItems().stream()
                .map( cqi ->   cqi.getElement().getXmlBuilder() )
                .collect( Collectors.toList() );

                children = concept.getChildren().stream()
                    .map( c -> c.getXmlBuilder() )
                    .collect( Collectors.toList() );
            }
        
        
            @Override
            public void addXmlFragments(Map<String, String> fragments) {
                fragments.putIfAbsent( getUrnId(), getXmlFragment() );
                for(AbstractXmlBuilder question: questions) {
                    question.addXmlFragments( fragments );
                }
                for(AbstractXmlBuilder child : children) {
                    child.addXmlFragments( fragments );
                }
            }
        
            public String getXmlFragment() {
                return String.format( xmlConcept,
                    getXmlURN(entity),
                    getXmlRationale(entity),
                    getXmlBasedOn(entity),
                    entity.getName(),
                    entity.getDescription(),
                    questions.stream()
                        .map( q -> q.getXmlEntityRef(3) )
                        .collect( Collectors.joining() ),
                    children.stream()
                        .map( c -> c.getXmlEntityRef(3) )
                        .collect( Collectors.joining()),
                    entity.getXmlLang());
            }
}
