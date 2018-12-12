package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.AbstractEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class XmlDDIFragmentAssembler<T extends AbstractEntity> {

    private final String XMLDEF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final String xmlFragHeader =
        "<ddi:FragmentInstance " +
        "xmlns:d=\"ddi:datacollection:3_2\" " +
        "xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" " +
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "xsi:schemaLocation=\"ddi:datacollection:3_2 http://www.ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/datacollection.xsd\">\n";

    private final T rootElement;
    private final AbstractXmlBuilder builder;

    private Map<UUID,String> fragments = new HashMap<>();

    public XmlDDIFragmentAssembler(T rootElement) {
        this.rootElement = rootElement;
        builder = rootElement.getXmlBuilder();
        builder.setEntityBody( fragments );
    }


    protected String getTopLevelReference(String typeOfObject){
        return "<ddi:TopLevelReference isExternal=\"false\" externalReferenceDefaultURI=\"false\" isReference=\"true\" lateBound=\"false\" objectLanguage=\"eng-GB\">\n" +
            "\t" + builder.getId() +
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
