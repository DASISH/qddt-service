package no.nsd.qddt.domain.category;

import no.nsd.qddt.classes.xml.XmlDDIFragmentBuilder;

/**
 * @author Stig Norland
 */
public class CategoryFragmentBuilder extends XmlDDIFragmentBuilder<Category> {

    private final String xmlCategory =
        "%1$s" +
        "\t\t\t<l:CategoryName>\n" +
        "\t\t\t\t<r:String %2$s>%3$s</r:String>\n" +
        "\t\t\t</l:CategoryName>\n" +
        "\t\t\t<r:Label>\n" +
        "\t\t\t\t<r:Content %2$s>%4$s</r:Content>\n" +
        "\t\t\t</r:Label>\n" +
        "%5$s";


//    d:ScaleDomainReference/r:TypeOfObject" defaultValue="ManagedScaleRepresentation" />
//    d:TextDomainReference/r:TypeOfObject" defaultValue="ManagedTextRepresentation" />
//    d:NumericDomainReference/r:TypeOfObject" defaultValue="ManagedNumericRepresentation"/>
//    d:DateTimeDomainReference/r:TypeOfObject" defaultValue="ManagedDateTimeRepresentation" />


    private final String xmlDomainReference =
        "%3$s<d:%1$sDomainReference isExternal=\"false\"  isReference=\"true\" lateBound=\"false\">\n" +
            "%3$s\t%2$s" +
            "%3$s\t<r:TypeOfObject>Managed%1$sRepresentation</r:TypeOfObject>\n" +
            "%3$s</d:%1$sDomainReference>\n";


    private final String xmlCodeDomain =
        "%2$s<d:CodeDomain>\n" +
        "%2$s\t<r:CodeListReference isExternal=\"false\"  isReference=\"true\" lateBound=\"false\">\n" +
        "%2$s\t\t%1$s" +
        "%2$s\t\t<r:TypeOfObject>CodeList</r:TypeOfObject>\n" +
        "%2$s\t</r:CodeListReference>\n"+
        "%2$s</d:CodeDomain>\n";


    public CategoryFragmentBuilder(Category category) {
        super(category);
    }


    @Override
    public String getXmlFragment() {
        return String.format( xmlCategory,
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
            return String.format( xmlCodeDomain,  getXmlURN(entity), getTabs( depth ) );
        else
            return String.format( xmlDomainReference, entity.getCategoryType().getName(), getXmlURN(entity),  getTabs( depth ));
    }

}
