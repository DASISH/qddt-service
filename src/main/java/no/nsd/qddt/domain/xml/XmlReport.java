package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.AbstractEntity;

/**
 * @author Stig Norland
 */
public class XmlReport {

    private AbstractEntity entity;
    private String ddiXmlRoot =
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

    public XmlReport(AbstractEntity entity) {
        this.entity = entity;
    }

    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append( ddiXmlRoot );
        sb.append( entity.toDDIXml() );
        return sb.toString();
    }


}
