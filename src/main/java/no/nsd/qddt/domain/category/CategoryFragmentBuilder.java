package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class CategoryFragmentBuilder extends XmlDDIFragmentBuilder<Category> {
    private final String xmlResponseCategory =
        "%1$s" +
            "\t<l:CategoryName>\n" +
            "\t\t<r:String %2$s>%3$s</r:String>\n" +
            "\t</l:CategoryName>" +
            "\t<r:Label>\n" +
            "\t\t<Content %2$s >%4$s</Content>\n" +
            "\t</r:Label>\n" +
        "%5$s";

    private final String xmlDomainReference =
        "<d:%1$sDomainReference isExternal=\"false\"  isReference=\"true\" lateBound=\"false\">\n" +
            "\t%2$s" +
            "\t<r:TypeOfObject>Managed%1$sRepresentation</r:TypeOfObject>\n" +
        "</d:%1$sDomainReference>";

    public CategoryFragmentBuilder(Category category) {
        super(category);
    }

    @Override
    public void addFragments(Map<UUID, String> fragments) {
        if (entity.getCategoryType() == CategoryType.CATEGORY)
            fragments.putIfAbsent( entity.getId(), getXmlFragment() );
    }

    @Override
    public String getXmlFragment() {
//        if (entity.getCategoryType() == CategoryType.CATEGORY)
        return String.format( xmlResponseCategory,
            getHeader( entity ),
            getXmlLang(entity),
            entity.getName(),
            entity.getLabel(),
            getFooter( entity )
            );
//        else
//            return "";

    }


    @Override
    public String getEntityRef() {
        if (entity.getCategoryType()== CategoryType.CATEGORY)
            return super.getEntityRef();
        return String.format( xmlDomainReference, entity.getCategoryType().getName(), getURN(entity));
    }

}
