package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.AbstractEntityAudit;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class XmlDDIFragmentAssembler<T extends AbstractEntityAudit> {

    private final String XMLDEF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final String xmlFragHeader =
        "<ddi:FragmentInstance \n" +
            "\txmlns:c=\"ddi:conceptualcomponent:3_2\" \n" +
            "\txmlns:d=\"ddi:datacollection:3_2\" \n" +
            "\txmlns:ddi=\"ddi:instance:3_2\" \n" +
            "\txmlns:g=\"ddi:group:3_2\" \n" +
            "\txmlns:l=\"ddi:logicalproduct:3_2\" \n" +
            "\txmlns:r=\"ddi:reusable:3_2\" \n" +
            "\txmlns:s=\"ddi:studyunit:3_2\" \n" +
            "\txmlns:xhtml=\"http://www.w3.org/1999/xhtml\" \n" +
            "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
            "\txsi:schemaLocation=\"ddi:instance:3_2  https://ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/instance.xsd\">\n";

    private final T rootElement;
    private final AbstractXmlBuilder builder;

    private Map<String,String> fragments = new HashMap<>();

    public XmlDDIFragmentAssembler(T rootElement) {
        this.rootElement = rootElement;
        builder = rootElement.getXmlBuilder();
        builder.addXmlFragments( fragments );
    }


    protected String getTopLevelReference(String typeOfObject){
        return "<ddi:TopLevelReference isExternal=\"false\" externalReferenceDefaultURI=\"false\" isReference=\"true\" lateBound=\"false\" objectLanguage=\"eng-GB\">\n" +
            "\t" + builder.getXmlURN(rootElement) +
            "\t<r:TypeOfObject>" + typeOfObject + "</r:TypeOfObject>\n" +
            "</ddi:TopLevelReference>\n";
    }

    protected String getFooter() {
        return "</ddi:FragmentInstance>\n";
    }


    public String compileToXml() {

        StringBuilder sb = new StringBuilder( );
        return sb.append( XMLDEF )
            .append( xmlFragHeader )
            .append( getTopLevelReference( (rootElement.getClass().getSimpleName() == "TopicGroup" ? "ConceptGroup" : rootElement.getClass().getSimpleName() ) ))
            .append( fragments.values().stream().collect( Collectors.joining("</ddi:Fragment>\n<ddi:Fragment>\n","<ddi:Fragment>\n","</ddi:Fragment>\n") ))
            .append( getFooter() )
            .toString();
    }


}
