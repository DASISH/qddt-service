package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.classes.xml.XmlDDIFragmentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class FragmentBuilderManageRep extends XmlDDIFragmentBuilder<Category> {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final String xmlScaleMan =
        "\t\t\t<r:ScaleDimension dimensionNumber=\"1\" degreeSlopeFromHorizontal=\"%1$s\">\n" +
        "\t\t\t\t<r:Range>\n" +
        "\t\t\t\t\t<r:RangeUnit>\"%2$s\"</r:RangeUnit>\n" +
        "\t\t\t\t\t<r:MinimumValue included=\"true\">\"%3$s\"</r:MinimumValue>\n" +
        "\t\t\t\t\t<r:MaximumValue included=\"true\">\"%4$s\"</r:MaximumValue>\n" +
        "\t\t\t\t</r:Range>\n" +
        "%5$s" +
        "\t\t\t</r:ScaleDimension>\n" ;

    private final String xmlMissingMan =
        "%1$s<r:MissingCodeRepresentation blankIsMissingValue =\"false\">\n" +
        "%1$s\t<r:CodeListReference>\n" +
        "%1$s\t\t%2$s"+
        "%1$s\t\t<r:TypeOfObject>CodeList</r:TypeOfObject>\n" +
        "%1$s\t</r:CodeListReference>\n" +
        "%1$s</r:MissingCodeRepresentation>\n";

    private final String xmlManaged =
        "%2$s" +
        "\t\t\t<r:Managed%1$sRepresentationName>\n" +
        "\t\t\t\t<r:String xml:lang=\"%3$s\">%4$s</r:String>\n" +
        "\t\t\t</r:Managed%1$sRepresentationName>\n" +
        "\t\t\t<r:Label>\n" +
        "\t\t\t\t<r:Content xml:lang=\"%3$s\">%6$s</r:Content>\n" +
        "\t\t\t</r:Label>\n" +
        "\t\t\t<r:Description>\n" +
        "\t\t\t\t<r:Content xml:lang=\"%3$s\">%5$s</r:Content>\n" +
        "\t\t\t</r:Description>\n" +
        "%7$s" +
        "\t\t</r:Managed%1$sRepresentation>\n";

//    d:CodeDomain/r:CodeListReference

    private final String xmlCodeList =
        "%2$s" +
        "\t\t\t<l:CodeListName>\n" +
        "\t\t\t\t<r:String xml:lang=\"%3$s\">%4$s</r:String>\n" +
        "\t\t\t</l:CodeListName>\n" +
        "\t\t\t<r:Label>\n" +
        "\t\t\t\t<r:Content xml:lang=\"%3$s\">%6$s</r:Content>\n" +
        "\t\t\t</r:Label>\n" +
        "\t\t\t<r:Description>\n" +
        "\t\t\t\t<r:Content xml:lang=\"%3$s\">%5$s</r:Content>\n" +
        "\t\t\t</r:Description>\n" +
        "%7$s" +
        "\t\t</l:CodeList>\n";


    private final String xmlMissingCodeListFragment =
        "%1$s<l:CodeList isUniversallyUnique=\"false\" scopeOfUniqueness=\"Maintainable\">\n" +
        "%1$s\t%2$s"+
        "%3$s"+
        "%1$s</l:CodeList>\n";

    protected final String xmlHeaderMR = "\t\t<%1$s isUniversallyUnique=\"true\" versionDate=\"%2$s\" isMaintainable=\"true\" %3$s>\n" +
        "%4$s";

    private final String xmlNumeric = " format=\"%1$s\" scale=1 interval=%2$s classificationLevel=\"Continuous\" ";
    private final String xmlText = " maxLength=%1$s minLength=%2$s classificationLevel=\"Nominal\" ";


    private final List<AbstractXmlBuilder> children;
    private final String degreeSlopeFromHorizontal;

    public FragmentBuilderManageRep(Category entity, String degreeSlopeFromHorizontal) {
        super( entity );
        this.degreeSlopeFromHorizontal = degreeSlopeFromHorizontal;
        children = entity.getChildren().stream()
        .map( this::getBuilder )
        .collect( Collectors.toList() );
    }


    @Override
    protected <S extends AbstractEntityAudit> String getXmlHeader(S instance){
        Category ref = (Category)instance;
        String attr =
            (entity.getCategoryType() == CategoryType.NUMERIC) ?
                String.format(xmlNumeric, ref.getFormat(), ref.getInputLimit().getStepUnit()) :
            (entity.getCategoryType() == CategoryType.TEXT) ?
                String.format(xmlText, ref.getInputLimit().getMaximum(), ref.getInputLimit().getMinimum()) :
                "";

        if (entity.getCategoryType().equals( CategoryType.LIST )) {
            return String.format(xmlHeaderMR,  "l:" + entity.getCategoryType().getName() ,  getInstanceDate(instance), "", "\t\t\t"+ getXmlURN(instance)+ getXmlUserId(instance)+ getXmlRationale(instance)+ getXmlBasedOn(instance));
        } else
            return String.format(xmlHeaderMR, "r:Managed" +  entity.getCategoryType().getName() + "Representation" ,  getInstanceDate(instance), attr, "\t\t\t"+ getXmlURN(instance)+ getXmlUserId(instance)+ getXmlRationale(instance)+ getXmlBasedOn(instance));
    }

    @Override
    public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {
       super.addXmlFragments( fragments );
       if (entity.getCategoryType() == CategoryType.MISSING_GROUP )
           fragments.get( ElementKind.CATEGORY ).putIfAbsent( getMissingCodeURN(), getXmlMissingCodeListFragment() );
       children.forEach(c -> c.addXmlFragments(fragments));
    }


    @Override
    public String getXmlFragment() {
        if (entity.getCategoryType() == CategoryType.LIST)
            return getXmlCodeList();
        else
            return String.format( xmlManaged,
            entity.getCategoryType().getName(),
            getXmlHeader( entity ),
            entity.getXmlLang(),
            entity.getName(),
            entity.getDescription(),
            entity.getLabel(),
            this.getXmlRefs());
    }

    private String getXmlRefs() {
        if (entity.getCategoryType().equals( CategoryType.SCALE ))
            return String.format( xmlScaleMan,
                degreeSlopeFromHorizontal,
                entity.getInputLimit().getStepUnit(),
                entity.getInputLimit().getMinimum(),
                entity.getInputLimit().getMaximum(),
                children.stream()
                    .map( q -> q.getXmlEntityRef(4) )
                    .collect( Collectors.joining() )
            );
        else if (entity.getCategoryType().equals( CategoryType.MISSING_GROUP ))
            return String.format( xmlMissingMan,getTabs( 3 ),getMissingCodeURN(),getMissingXmlEntityRef(4));
        else
            return children.stream()
                .map( q -> q.getXmlEntityRef(3) )
                .collect( Collectors.joining() );
    }

    private String getXmlCodeList() {
        return String.format( xmlCodeList,
            entity.getCategoryType().getName(),
            getXmlHeader( entity ),
            entity.getXmlLang(),
            entity.getName(),
            entity.getDescription(),
            entity.getLabel(),
            children.stream()
                .map( q -> q.getXmlEntityRef(3) )
                .collect( Collectors.joining() ));
    }


    private AbstractXmlBuilder getBuilder(Category category) {
        switch (entity.getCategoryType()) {
            case SCALE:
                return new FragmentBuilderAnchor(category);
            case MISSING_GROUP:
            case LIST:
                return new FragmentBuilderCode(category);
            default:
                LOG.info("getbuilder get default");
                return category.getXmlBuilder();
        }
    }

    private String getMissingCodeURN() {
        return  String.format( xmlURN, entity.getAgency().getName(),entity.getName(),entity.getVersion().toDDIXml());
    }

    private String getXmlMissingCodeListFragment() {
        return String.format( xmlMissingCodeListFragment,
            getTabs( 2 ),
            getMissingCodeURN(),
            children.stream()
                .map( q -> q.getXmlEntityRef(3) )
                .collect( Collectors.joining() ));

    }

    private String getMissingXmlEntityRef(int depth) {
        return String.format( xmlMissingMan, getTabs( depth ), getMissingCodeURN() );
    }


}
