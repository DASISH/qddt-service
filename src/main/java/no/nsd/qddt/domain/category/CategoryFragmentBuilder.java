package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;

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
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId(), getXmlFragment() );
    }

    @Override
    public String getXmlFragment() {
//        if (entity.getCategoryType() == CategoryType.CATEGORY)
        return String.format( xmlResponseCategory,
            getXmlHeader( entity ),
            getXmlLang(entity),
            entity.getName(),
            entity.getLabel(),
            getXmlFooter( entity )
            );
//        else
//            return "";

    }



    @Override
    public String getXmlEntityRef() {
        if (entity.getCategoryType()== CategoryType.CATEGORY)
            return super.getXmlEntityRef();
        return String.format( xmlDomainReference, entity.getCategoryType().getName(), getXmlURN(entity));
    }

}
