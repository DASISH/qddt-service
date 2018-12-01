package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.AbstractEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class XmlFragmentAssembler<T extends AbstractEntity> {

    private final String XMLDEF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final String xmlFragHeader =  "<ddi:FragmentInstance " +
        "xmlns:a=\"ddi:archive:3_2\" " +
        "xmlns:c=\"ddi:conceptualcomponent:3_2\" " +
        "xmlns:cm=\"ddi:comparative:3_2\" " +
        "xmlns:d=\"ddi:datacollection:3_2\" " +
        "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
        "xmlns:dcmitype=\"http://purl.org/dc/dcmitype/\" " +
        "xmlns:dcterms=\"http://purl.org/dc/terms/\" " +
        "xmlns:ddi=\"ddi:instance:3_2\" " +
        "xmlns:ds=\"ddi:dataset:3_2\" " +
        "xmlns:g=\"ddi:group:3_2\" " +
        "xmlns:l=\"ddi:logicalproduct:3_2\" " +
        "xmlns:nc1=\"ddi:physicaldataproduct_ncube_normal:3_2\" " +
        "xmlns:nc2=\"ddi:physicaldataproduct_ncube_tabular:3_2\" " +
        "xmlns:nc3=\"ddi:physicaldataproduct_ncube_inline:3_2\" " +
        "xmlns:p=\"ddi:physicaldataproduct:3_2\" " +
        "xmlns:pi=\"ddi:physicalinstance:3_2\" " +
        "xmlns:pp=\"ddi:physicaldataproduct_proprietary:3_2\" " +
        "xmlns:pr=\"ddi:ddiprofile:3_2\" " +
        "xmlns:r=\"ddi:reusable:3_2\" " +
        "xmlns:s=\"ddi:studyunit:3_2\" " +
        "xmlns:html=\"http://www.w3.org/1999/xhtml\" " +
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "xsi:schemaLocation=\"ddi:instance:3_2  http://www.ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/instance.xsd\">\n";

    private final T rootElement;
    private final AbstractXmlBuilder builder;

    private Map<UUID,String> fragments = new HashMap<>();

    public XmlFragmentAssembler(T rootElement) {
        this.rootElement = rootElement;
        builder = rootElement.getXmlBuilder();
        builder.setEntityBody( fragments );
    }


    protected String getTopLevelReference(){
        return "<ddi:TopLevelReference isExternal=\"false\" externalReferenceDefaultURI=\"false\" isReference=\"true\" lateBound=\"false\" objectLanguage=\"eng-GB\">\n" +
            "\t" + builder.getId() +
            "\t<r:TypeOfObject>" + rootElement.getClass().getSimpleName() + "</r:TypeOfObject>\n" +
            "</ddi:TopLevelReference>\n";
    }

    protected String getFooter() {
        return "</ddi:FragmentInstance>\n";
    }


    public String compileToXml() {

        StringBuilder sb = new StringBuilder( );
        return sb.append( XMLDEF )
            .append( xmlFragHeader )
            .append( getTopLevelReference() )
            .append( fragments.values().stream().collect( Collectors.joining("</ddi:Fragment>\n<ddi:Fragment>\n","<ddi:Fragment>\n","</ddi:Fragment>\n") ))
            .append( getFooter() )
            .toString();
    }


}
