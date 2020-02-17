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

    @Override
    public String getEntityRef() {
        return String.format( xmlRef,  entity.getClass().getSimpleName(), getURN(entity) );
    }


}
