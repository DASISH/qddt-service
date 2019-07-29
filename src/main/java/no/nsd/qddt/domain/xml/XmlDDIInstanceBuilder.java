package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.AbstractEntityAudit;

import java.util.Map;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class XmlDDIInstanceBuilder<T extends AbstractEntityAudit> extends AbstractXmlBuilder<T> {

    private final String ddiXmlRoot =
        "<DDIInstance \n" +
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

    public XmlDDIInstanceBuilder(T entity) {
        super( entity );
    }

    @Override
    protected String getId() {
        return null;
    }

    @Override
    public void setEntityBody(Map<UUID, String> fragments) {

    }

    @Override
    public String getEntityRef() {
        return null;
    }


}