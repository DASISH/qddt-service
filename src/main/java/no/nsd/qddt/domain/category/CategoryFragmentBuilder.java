package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Collections;

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
        "\t\t\t\t<r:Content %2$s>%4$s</r:Content>\n" +
        "\t\t\t</r:Label>\n" +
        "%5$s";

    private final String xmlDomainReference =
        "%3$s<d:%1$sDomainReference isExternal=\"false\"  isReference=\"true\" lateBound=\"false\">\n" +
        "%3$s\t%2$s" +
        "%3$s\t<r:TypeOfObject>Managed%1$sRepresentation</r:TypeOfObject>\n" +
        "%3$s</d:%1$sDomainReference>\n";

    private final String xmlCodeDomain =
        "%2$s<d:CodeDomain>\n" +
        "%2$s\t<r:CodelistReference isExternal=\"false\"  isReference=\"true\" lateBound=\"false\">\n" +
        "%2$s\t\t%1$s" +
        "%2$s\t\t<r:TypeOfObject>Codelist</r:TypeOfObject>\n" +
        "%2$s\t</r:CodelistReference>\n"+
        "%2$s</d:CodeDomain>\n";

    public CategoryFragmentBuilder(Category category) {
        super(category);
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
        else if (entity.getCategoryType()== CategoryType.LIST)
            return String.format( xmlCodeDomain,  getXmlURN(entity), String.join("", Collections.nCopies(depth, "\t")) );
        else
            return String.format( xmlDomainReference, entity.getCategoryType().getName(), getXmlURN(entity),  String.join("", Collections.nCopies(depth, "\t")));
    }

}
