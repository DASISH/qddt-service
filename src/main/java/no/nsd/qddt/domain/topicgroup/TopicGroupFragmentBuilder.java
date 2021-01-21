package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.concept.ConceptFragmentBuilder;
import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.questionitem.QuestionItemFragmentBuilder;
import no.nsd.qddt.domain.classes.xml.XmlDDIFragmentBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class TopicGroupFragmentBuilder extends XmlDDIFragmentBuilder<TopicGroup> {

    private final String xmlTopic =
        "\t\t<c:ConceptGroup isOrdered= \"false\" isAdministrativeOnly=\"true\">\n" +
        "\t\t\t%1$s" +
        "%2$s" +
        "%3$s" +
        "\t\t\t<c:ConceptGroupName>\n" +
        "\t\t\t\t<r:String xml:lang=\"%6$s\">%4$s</r:String>\n" +
        "\t\t\t</c:ConceptGroupName>\n" +
        "\t\t\t<r:Description>\n" +
        "\t\t\t\t<r:Content xml:lang=\"%6$s\" isPlainText=\"false\">%5$s</r:Content>\n" +
        "\t\t\t</r:Description>\n" +
        "%7$s" +
        "\t\t</c:ConceptGroup>\n";

    private final List<ConceptFragmentBuilder> children;
    private final List<QuestionItemFragmentBuilder> questions;


    public TopicGroupFragmentBuilder(TopicGroup topicGroup) {
        super(topicGroup);
        children = topicGroup.getConcepts().stream().map( c -> (ConceptFragmentBuilder)c.getXmlBuilder() ).collect( Collectors.toList() );
        questions = topicGroup.getTopicQuestionItems().stream()
            .filter(f ->  f.getElement() != null )
            .map( cqi -> (QuestionItemFragmentBuilder)cqi.getElement().getXmlBuilder() )
            .collect( Collectors.toList() );
    }


    @Override
    public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {
        super.addXmlFragments( fragments );
//        fragments.putIfAbsent( getUrnId(), getXmlFragment() );
        for(ConceptFragmentBuilder child : children) {
            child.addXmlFragments( fragments );
        }
        for(QuestionItemFragmentBuilder question: questions) {
            question.addXmlFragments( fragments );
        }
    }

    @Override
    public String getXmlEntityRef(int depth) {
        return String.format( xmlRef,  "ConceptGroup", getXmlURN(entity)  , String.join("", Collections.nCopies(depth, "\t")) );
    }

    public String getXmlFragment() {
        return String.format( xmlTopic,
            getXmlURN(entity),
            getXmlRationale(entity),
            getXmlBasedOn(entity),
            entity.getName(),
            entity.getDescription(),
            entity.getXmlLang(),
            children.stream()
                .map(c -> c.getXmlEntityRef(3))
                .collect( Collectors.joining()) );
    }

}
