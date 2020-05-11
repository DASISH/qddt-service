package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//		<r:ManagedScaleRepresentation scopeOfUniqueness="Agency" isUniversallyUnique="true" versionDate="2017-02-01" isVersionable="true">
//
//			<r:ManagedScaleRepresentationName><r:String xml:lang="en-GB">0 - 10 EXTR BAD VS: EXTR GOOD</r:String></r:ManagedScaleRepresentationName>
//			<r:Description>
//				<r:Content xml:lang="en-GB">Eleven point scale with two anchor points ranging from extremely bad to extremely good</r:Content>
//			</r:Description>

//			<r:ScaleDimension dimensionNumber="1" degreeSlopeFromHorizontal="0">
//				<r:NumberRange>
//					<r:Low>0</r:Low>
//					<r:High>10</r:High>
//				</r:NumberRange>

//				<r:Anchor value="0">
//					<r:CategoryReference>
//						<r:URN>urn:ddi:int.esseric:80e1f38c-1c54-4d74-91d9-7c3590b194b9:1.0</r:URN>
//						<r:TypeOfObject>Category</r:TypeOfObject>
//					</r:CategoryReference>
//				</r:Anchor>

//				<r:Anchor value="10">
//					<r:CategoryReference>
//						<r:URN>urn:ddi:int.esseric:c91ec751-d300-4069-8429-7eb98578f222:1.0</r:URN>
//						<r:TypeOfObject>Category</r:TypeOfObject>
//					</r:CategoryReference>
//				</r:Anchor>
//			</r:ScaleDimension>
//		</r:ManagedScaleRepresentation>

//  <l:CodeList scopeOfUniqueness="Agency" isUniversallyUnique="true" versionDate="2017-02-01" externalReferenceDefaultURI="" isPublished="true" xml:lang="en-GB" isMaintainable="true" isSystemMissingValue="false">

//			<l:CodeListName><r:String>REFDK</r:String></l:CodeListName>
//			<r:Label><r:Content>Refusal; Don't know</r:Content></r:Label>
//			<r:Description><r:Content>Code list containing missing values refusal and don't know</r:Content></r:Description>

//			<l:Code scopeOfUniqueness="Agency" isUniversallyUnique="true" isIdentifiable="true" isDiscrete="true" levelNumber="1"  isTotal="false">
//				<r:URN>urn:ddi:int.esseric:96d6d419-7728-4b08-8f2e-fb1ea3c5f001:1.0</r:URN>
//				<r:CategoryReference>
//					<r:URN>urn:ddi:int.esseric:7afd5070-c5b3-45a3-adc5-6e2574c10259:1.0</r:URN>
//					<r:TypeOfObject>Category</r:TypeOfObject>
//				</r:CategoryReference>
//				<r:Value xml:space="default">7</r:Value>
//			</l:Code>

//			<l:Code scopeOfUniqueness="Agency" isUniversallyUnique="true" isIdentifiable="true" isDiscrete="true" levelNumber="1"  isTotal="false">
//				<r:URN>urn:ddi:int.esseric:49ef0909-bb2b-4b8d-9745-0ac3b2884829:1.0</r:URN>
//				<r:CategoryReference>
//					<r:URN>urn:ddi:int.esseric:4b959dbe-272b-4868-9cc0-62f6d416fffb:1.0</r:URN>
//					<r:TypeOfObject>Category</r:TypeOfObject>
//				</r:CategoryReference>
//				<r:Value xml:space="default">8</r:Value>
//			</l:Code>
//		</l:CodeList>


/**
 * @author Stig Norland
 */
public class FragmentBuilderManageRep extends XmlDDIFragmentBuilder<Category> {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final String xmlManaged =
//        "\t\t<r:Managed%1$sRepresentation scopeOfUniqueness=\"Agency\" isUniversallyUnique=\"true\" versionDate=\"%2$s\" isVersionable=\"true\">\n" +
        "%9$s" +
        "\t\t\t<r:Managed%1$sRepresentationName>\n" +
        "\t\t\t\t<r:String %3$s>%4$s</r:String>\n" +
        "\t\t\t</r:Managed%1$sRepresentationName>\n" +
        "\t\t\t<r:Description>\n" +
        "\t\t\t\t<r:Content %3$s>%5$s</r:Content>\n" +
        "\t\t\t</r:Description>\n" +
        "\t\t\t<r:ScaleDimension dimensionNumber=\"1\" degreeSlopeFromHorizontal=\"%6$s\">\n" +
        "\t\t\t\t<r:NumberRange>\n" +
        "\t\t\t\t\t<r:Low>%7$s</r:Low>\n" +
        "\t\t\t\t\t<r:High>%8$s</r:High>\n" +
        "\t\t\t\t</r:NumberRange>\n" +
        "%10$s" +
        "\t\t\t</r:ScaleDimension>\n" +
        "\t\t</r:Managed%1$sRepresentation>\n";

    private final List<AbstractXmlBuilder> children;
    private final String degreeSlopeFromHorizontal;

    public FragmentBuilderManageRep(Category entity, String degreeSlopeFromHorizontal) {
        super( entity );
        this.degreeSlopeFromHorizontal = degreeSlopeFromHorizontal;
        children = entity.getChildren().stream()
        .map( c -> getBuilder(c) )
        .collect( Collectors.toList() );
    }


    @Override
    protected <S extends AbstractEntityAudit> String getXmlHeader(S instance){
        return String.format(xmlHeader, entity.getCategoryType().getName(), instance.getModified(), "\t\t\t"+ getXmlURN(instance)+ getXmlUserId(instance)+ getXmlRationale(instance)+ getXmlBasedOn(instance));
    }

    @Override
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId() , getXmlFragment() );
        children.forEach(c -> c.addXmlFragments(fragments));
    }

    @Override
    public String getXmlFragment() {
        return String.format( xmlManaged,
        entity.getCategoryType().getName(),
        entity.getModified(),
        entity.getXmlLang(),
        entity.getName(),
        entity.getDescription(),
        degreeSlopeFromHorizontal,
        entity.getInputLimit().getMinimum(),
        entity.getInputLimit().getMaximum(),
        getXmlHeader( entity ),
        children.stream()
            .map( q -> q.getXmlEntityRef(4) )
            .collect( Collectors.joining() )
        );
    }

    @Override
    public String getXmlEntityRef(int depth) {
        return String.format( xmlRef,  entity.getCategoryType().getName(), getXmlURN(entity)  , String.join("", Collections.nCopies(depth, "\t")) );
    }


    private AbstractXmlBuilder getBuilder(Category category) {
        LOG.info(entity.getCategoryType().getName() );
        switch (entity.getCategoryType()) {
            case SCALE:
                return new FragmentBuilderAnchor(category);
            case LIST:
                return new FragmentBuilderCode(category);
            default:
                LOG.info("getbuilder get default");
                return category.getXmlBuilder();
        }
    }


}
