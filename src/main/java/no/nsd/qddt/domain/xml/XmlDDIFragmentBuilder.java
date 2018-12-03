package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.AbstractEntityAudit;

/**
 * @author Stig Norland
 */
public abstract class XmlDDIFragmentBuilder<T extends AbstractEntityAudit> extends AbstractXmlBuilder<T> {

    protected String xmlTagPreFix= "r:";

    protected final String xmlURN1 =
        "<r:URN type=\"URN\" typeOfIdentifier=\"Canonical\">urn:ddi:%1$s:%2$s:%3$s</r:URN>\n";

    protected final String xmlRef =
        "<%1$s%2$sReference>\n" +
        "\t%3$s" +
        "\t<%1$sTypeOfObject>%2$s</%1$sTypeOfObject>\n" +
        "</%1$s%2$sReference>\n";

    private final String xmlRationale =
        "<r:VersionResponsibility>%1$s</r:VersionResponsibility>\n" +
        "<r:VersionRationale>\n" +
        "\t<r:RationaleDescription>\n" +
        "\t\t<r:String>%2$s</r:String>\n" +
        "\t</r:RationaleDescription>\n" +
        "\t<r:RationaleCode>%3$s</r:RationaleCode>\n" +
        "</r:VersionRationale>\n";

    private final String xmlBasedOn =
        "<r:BasedOnReference>\n" +
        "\t%1$s" +
        "\t<r:TypeOfObject>%2$s</r:TypeOfObject>\n"+
        "</r:BasedOnReference>\n";


    public XmlDDIFragmentBuilder(T entity) {
        super(entity);
    }


    protected String getRationale() {
        return  String.format( xmlRationale, entity.getModifiedBy().getAgencyUserName(), entity.getChangeComment(), entity.getChangeKind().name() );
    }

    protected String getBasedOn() {
        if (entity.getBasedOnObject() == null) return "";
        return String.format( xmlBasedOn, xmlTagPreFix,getId(),entity.getClass().getSimpleName() );
    }


    @Override
    protected String getId() {
        return  String.format(xmlURN1, entity.getAgency().getName(),entity.getId(),entity.getVersion().toDDIXml());
    }


    @Override
    public String getEntityRef() {
        return String.format( xmlRef, xmlTagPreFix, entity.getClass().getSimpleName(), getId() );
    }

}
