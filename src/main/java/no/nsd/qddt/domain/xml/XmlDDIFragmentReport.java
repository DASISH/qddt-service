package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.AbstractEntityAudit;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class XmlDDIFragmentReport extends AbstractXmlReport {

    private final String xmlPre = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final String xmlFragInst =  "<ddi:FragmentInstance " +
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
        "xsi:schemaLocation=\"ddi:instance:3_2  http://www.ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/instance.xsd\">" +
        "%s" +
        "</ddi:FragmentInstance>";
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

    private final String xmlURN1 =
        "\t<r:URN>urn:ddi:%1$s:%2$s:%3$s</r:URN>\n";

    private final String xmlURN2 =
        "\t<r:Agency>%1$s</r:Agency>\n" +
        "\t<r:ID>%2$s</r:ID>\n" +
        "\t<r:Version>%3$s</r:Version>\n";

    private final String xmlRef =
        "<%1$s%2$sReference>\n" +
        "\t%3$s" +
        "\t<%1$sTypeOfObject>%2$s</%1$sTypeOfObject>\n" +
        "</%1$s%2$sReference>\n";

    private final String xmlRationale =
        "<r:VersionResponsibility>%1$s</r:VersionResponsibility>\n" +
        "\t<r:VersionRationale>\n" +
        "\t\t<r:RationaleDescription>\n" +
        "\t\t\t<r:String>%2$s</r:String>\n" +
        "\t\t</r:RationaleDescription>\n" +
        "\t\t\t<r:RationaleCode>%3$s</r:RationaleCode>\n" +
        "\t</r:VersionRationale>";

    private final String xmlBasedOn =
        "<r:BasedOnReference>\n" +
        "\t%1$s" +
        "\t<r:TypeOfObject>%2$s</r:TypeOfObject>\n"+
        "</r:BasedOnReference>\n";


    public XmlDDIFragmentReport() {
    }

    public void addFragment(UUID id, String xmlfragment) {
        if (!this.fragments.containsKey(id ))
            this.fragments.put( id, xmlfragment );
    }

    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append( ddiXmlRoot );
        sb.append( entity.toDDIXml() );
        return sb.toString();
    }

    public String getURN1(AbstractEntityAudit entity){
        return  String.format(xmlURN1, entity.getAgency().getName(),entity.getId(),entity.getVersion().toDDIXml());
    }

    public String getURN2(AbstractEntityAudit entity){
        return  String.format(xmlURN2,
            entity.getAgency().getName(),
            entity.getId(),
            entity.getVersion().toDDIXml());
    }

    public String getREF(String prefix, String className, AbstractEntityAudit entity){
        return  String.format(xmlRef, prefix, className, getURN1(entity ));
    }

    public String getRationale(AbstractEntityAudit entity) {
        return  String.format( xmlRationale, entity.getModifiedBy().getUsername(), entity.getChangeComment(), entity.getChangeKind());
    }

    public String getXmlBasedOn(AbstractEntityAudit entity, String className) {
        return String.format( xmlBasedOn, getURN1( entity ), className );
    }


}
