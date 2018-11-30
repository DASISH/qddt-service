package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.UUID;

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

    public ConceptFragmentBuilder(Concept concept) {
        super(concept);
    }

    @Override
    public void setEntityBody(Map<UUID, String> fragments) {

    }
}
