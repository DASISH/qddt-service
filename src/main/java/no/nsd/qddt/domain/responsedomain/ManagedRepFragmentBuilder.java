package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ManagedRepFragmentBuilder extends XmlDDIFragmentBuilder<Category> {
    private final String xmlResponseCategory =
        "%1$s" +
            "\t<r:Label>\n" +
            "\t\t<Content %2$s >%3$s</Content>\n" +
            "\t</Label>\n" +
            "%3$s";

    private final String xmlDomainReference =
        "<d:%1$sDomainReference isExternal=\"false\"  isReference=\"true\" lateBound=\"false\">\n" +
            "\t%2$s" +
            "\t<r:TypeOfObject>Managed%1$sRepresentation</r:TypeOfObject>\n" +
            "</d:%1$sDomainReference>";


    public ManagedRepFragmentBuilder(Category entity) {
        super( entity );
    }

    @Override
    public void addFragments(Map<UUID, String> fragments) {
        if (entity.getCategoryType() != CategoryType.CATEGORY)
            fragments.putIfAbsent( entity.getId(), getXmlFragment() );
    }

    @Override
    public String getXmlFragment() {
        return String.format( xmlResponseCategory,
            getHeader( entity ),
            getXmlLang(entity),
            entity.getLabel(),
            getFooter( entity )
        );
    }

    @Override
    public String getEntityRef() {
        if (entity.getCategoryType()== CategoryType.CATEGORY)
            return super.getEntityRef();
        return String.format( xmlDomainReference, entity.getCategoryType().getName(), getURN(entity));
    }

}
