package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;

/**
 * @author Stig Norland
 */
public class CategoryFragmentBuilder extends XmlDDIFragmentBuilder<Category> {
    private final String xmlResponseCategory =
        "%1$s" +
        "\t\t\t<l:CategoryName>\n" +
        "\t\t\t\t<r:String %2$s>%3$s</r:String>\n" +
        "\t\t\t</l:CategoryName>\n" +
        "\t\t\t<r:Label>\n" +
        "\t\t\t\t<Content %2$s >%4$s</Content>\n" +
        "\t\t\t</r:Label>\n" +
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
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId(), getXmlFragment() );
    }

    @Override
    public String getXmlFragment() {
        return String.format( xmlResponseCategory,
            getXmlHeader( entity ),
            getXmlLang(entity),
            entity.getName(),
            entity.getLabel(),
            getXmlFooter( entity )
            );
    }



    @Override
    public String getXmlEntityRef(int depth) {
        if (entity.getCategoryType()== CategoryType.CATEGORY)
            return super.getXmlEntityRef(depth);
        return String.format( xmlDomainReference, entity.getCategoryType().getName(), getXmlURN(entity), depth);
    }

}
