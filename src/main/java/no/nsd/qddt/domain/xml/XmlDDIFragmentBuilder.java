package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.elementref.ElementKind;

import java.util.Collections;
import java.util.Map;

/**
 * @author Stig Norland
 */
public abstract class XmlDDIFragmentBuilder<T extends AbstractEntityAudit> extends AbstractXmlBuilder {

    protected final T entity;

    protected final String xmlRef =
        "%3$s<r:%1$sReference>\n" +
        "%3$s\t%2$s" +
        "%3$s\t<r:TypeOfObject>%1$s</r:TypeOfObject>\n" +
        "%3$s</r:%1$sReference>\n";


    public XmlDDIFragmentBuilder(T entity) {
        this.entity = entity;
    }

    @Override
    public void addXmlFragments( Map<ElementKind, Map<String,String>> fragments) {
        ElementKind kind=null;
        try {
            kind = ElementKind.getEnum( entity.getClass().getSimpleName() );
        } catch (Exception ex) {
            kind = ElementKind.getEnum( entity.getClassKind() );
        } finally {
            fragments.get( kind ).putIfAbsent( getUrnId(), getXmlFragment() );
        }
    }

    public String getUrnId() {
        return  String.format( "%1$s:%2$s:%3$s",  entity.getAgency().getName() , entity.getId() , entity.getVersion().toDDIXml());
    }

    @Override
    public String getXmlEntityRef(int depth) {
        return String.format( xmlRef,  entity.getClass().getSimpleName(), getXmlURN(entity)  , String.join("", Collections.nCopies(depth, "\t")) );
    }


}
