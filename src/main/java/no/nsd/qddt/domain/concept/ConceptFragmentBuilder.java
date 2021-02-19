package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.classes.xml.XmlDDIFragmentBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConceptFragmentBuilder extends XmlDDIFragmentBuilder<Concept> {

    private final String xmlConcept =
        "%1$s"+
        "\t\t\t<c:ConceptName>\n" +
        "\t\t\t\t<r:String xml:lang=\"%5$s\">%2$s</r:String>\n" +
        "\t\t\t</c:ConceptName>\n"+
        "\t\t\t<r:Description>\n" +
        "\t\t\t\t<r:Content xml:lang=\"%5$s\" isPlainText=\"false\"><![CDATA[%3$s]]></r:Content>\n" +
        "\t\t\t</r:Description>\n" +
        "%4$s" +
        "\t\t</c:Concept>\n";

            private final List<AbstractXmlBuilder> children;
            private final List<AbstractXmlBuilder> questions;

            public ConceptFragmentBuilder(Concept concept) {
                super(concept);
                // TODO fix questions, add them to export, then reference Concept
                questions = concept.getConceptQuestionItems().stream()
                .filter(f ->  f.getElement() != null )
                .map( cqi ->   cqi.getElement().getXmlBuilder() )
                .collect( Collectors.toList() );

                children = concept.getChildren().stream()
                    .map( c -> c.getXmlBuilder() )
                    .collect( Collectors.toList() );
            }
 
            @Override
            protected <S extends AbstractEntityAudit> String getXmlHeader(S instance){
                String prefix = ElementKind.getEnum(instance.getClass().getSimpleName()).getDdiPreFix();
                String child =  (((Concept)instance).getChildren().isEmpty()) ? "" : " isCharacteristic =\"true\"";
                return String.format(xmlHeader, prefix, 
                    instance.getClass().getSimpleName(),
                    getInstanceDate(instance),
                    child,
                    "\t\t\t"+ getXmlURN(instance)+ getXmlUserId(instance)+ getXmlRationale(instance)+ getXmlBasedOn(instance));
            }        
            @Override
            public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {
                super.addXmlFragments( fragments );
                for(AbstractXmlBuilder question: questions) {
                    question.addXmlFragments( fragments );
                }
                for(AbstractXmlBuilder child : children) {
                    child.addXmlFragments( fragments );
                }
            }
        
            public String getXmlFragment() {
                return String.format( xmlConcept,
                    getXmlHeader(entity),
                    entity.getName(),
                    entity.getDescription(),
                    children.stream()
                        .map( c -> c.getXmlEntityRef(3) )
                        .collect( Collectors.joining()),
                    entity.getXmlLang());
            }
}
