package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.AbstractEntityAudit;

/**
 * @author Stig Norland
 */
public abstract class XmlDDIFragmentBuilder<T extends AbstractEntityAudit> extends AbstractXmlBuilder {

    protected final T entity;

    protected final String xmlRef =
        "<r:%1$sReference>\n" +
        "\t%2$s" +
        "\t<r:TypeOfObject>%1$s</r:TypeOfObject>\n" +
        "</r:%1$sReference>\n";


    public XmlDDIFragmentBuilder(T entity) {
        this.entity = entity;
    }


    public String getUrnId() {
        return  String.format( "%1$s:%2$s:%3$s",  entity.getAgency().getName() , entity.getId() , entity.getVersion().toDDIXml());
    }

    @Override
    public String getXmlEntityRef() {
        return String.format( xmlRef,  entity.getClass().getSimpleName(), getXmlURN(entity) );
    }


}
