package no.nsd.qddt.domain.classes.xml;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.classes.elementref.ElementKind;

import java.util.Map;

/**
 * @author Stig Norland
 */
public class XmlDDIInstanceBuilder<T extends AbstractEntityAudit> extends AbstractXmlBuilder {

    protected final T instance;

    private final String ddiXmlRoot =
        "\n<DDIInstance \n" +
            "    xmlns:g=\"ddi:group:3_2\" \n" +
            "    xmlns:d=\"ddi:datacollection:3_2\"\n" +
            "    xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "    xmlns:r=\"ddi:reusable:3_2\"\n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xmlns:l=\"ddi:logicalproduct:3_2\"\n" +
            "    xmlns:pr=\"ddi:ddiprofile:3_2\"\n" +
            "    xmlns =\"ddi:instance:3_2\"\n" +
            "    xmlns:s=\"ddi:studyunit:3_2\"\n" +
            "    xsi:schemaLocation=\"ddi:instance:3_2 http://www.ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/instance.xsd\">\n";

    public XmlDDIInstanceBuilder(T instance) {
       this.instance = instance;
    }

    protected String getId() {
        return null;
    }


    @Override
    public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {

    }

    @Override
    public String getXmlEntityRef(int depth) {
        return null;
    }

    @Override
    public String getXmlFragment() {
        return null;
    }


}
