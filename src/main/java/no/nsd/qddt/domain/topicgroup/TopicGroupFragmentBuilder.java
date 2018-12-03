package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.concept.ConceptFragmentBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class TopicGroupFragmentBuilder extends XmlDDIFragmentBuilder<TopicGroup> {

    private final String xmlTopic =
        "<d:ConceptGroup isOrdered= \"false\" isAdministrativeOnly=\"true\">\n" +
            "\t%1$s" +
            "\t%2$s" +
            "\t%3$s" +
            "\t<r:ConceptGroupName maxLength=\"250\">\n" +
            "\t\t<r:Content xml:lang=\"%6$s\">%4$s</r:Content>\n" +
            "\t</r:ConceptGroupName>\n" +
            "\t<r:Description maxLength=\"10000\">\n" +
            "\t\t<r:Content xml:lang=\"%6$s\" isPlainText=\"false\">%5$s</r:Content>\n" +
            "\t</r:Description>\n" +
            "\t%7$s" +
        "</d:ConceptGroup>\n";

    private List<ConceptFragmentBuilder> children;

    public TopicGroupFragmentBuilder(TopicGroup topicGroup) {
        super(topicGroup);
        children = topicGroup.getConcepts().stream().map( c -> (ConceptFragmentBuilder)c.getXmlBuilder() ).collect( Collectors.toList() );
    }


    @Override
    public void setEntityBody(Map<UUID, String> fragments) {
        fragments.putIfAbsent( entity.getId(), getXmlBody() );
        for(ConceptFragmentBuilder child : children) {
            child.setEntityBody( fragments );
        }
    }

    private String getXmlBody() {
        return String.format( xmlTopic,
            getId(),
            getRationale(),
            getBasedOn(),
            entity.getName(),
            entity.getDescription(),
            entity.getXmlLang(),
            children.stream()
                .map(c -> c.getEntityRef())
                .collect( Collectors.joining()) );
    }

}
