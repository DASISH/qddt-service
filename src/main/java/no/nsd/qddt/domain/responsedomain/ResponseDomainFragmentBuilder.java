package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ResponseDomainFragmentBuilder extends XmlDDIFragmentBuilder<ResponseDomain> {

//    private Map<UUID,Category> rdContent;

    private List<AbstractXmlBuilder> children;

    private final String xmlBody =
        "%1$s" +
            "\t%2$s" +
        "%3$s";

    private final String xmlMixedRef =
        "<d:StructuredMixedResponseDomain>\n" +
        "\t%1$s" +
        "</d:StructuredMixedResponseDomain>\n";

    private final String xmlInMixed =
        "<d:ResponseDomainInMixed>\n" +
        "\t%1$s" +
        "</d:ResponseDomainInMixed>\n";

    private final String xmlManaged =
        "<r:Managed%1$sRepresentation scopeOfUniqueness=\"Agency\" isUniversallyUnique=\"true\" versionDate=\"%2$s\" isVersionable=\"true\">\n" +
            "\t%3$s" +
            "\t<r:Managed%1$sRepresentationName><r:String %4$s>%5$s</r:String></r:Managed%1$sRepresentationName>\n" +
            "\t\t<r:Description>\n" +
            "\t\t\t<r:Content %4$s>%6$s</r:Content>\n" +
            "\t\t</r:Description>\n" +
            "\t\t<r:ScaleDimension dimensionNumber=\"1\" degreeSlopeFromHorizontal=\"%7$s\">\n" +
            "\t\t\t<r:NumberRange>\n" +
            "\t\t\t\t<r:Low>%8$s</r:Low>\n" +
            "\t\t\t\t<r:High>%9$s</r:High>\n" +
            "\t\t\t</r:NumberRange>\n" +
            "\t\t\t%10$s" +
            "\t\t\t</r:ScaleDimension>\n" +
        "</r:Managed%1$sRepresentation>";


//    d:ScaleDomainReference/r:TypeOfObject" defaultValue="ManagedScaleRepresentation" fixedValue="true"/>
//    d:CodeDomain/r:CodeListReference/r:TypeOfObject" defaultValue="CodeList" fixedValue="true"/>
//    d:TextDomainReference/r:TypeOfObject" defaultValue="ManagedTextRepresentation" fixedValue="true"/>
//    d:NumericDomainReference/r:TypeOfObject" defaultValue="ManagedNumericRepresentation" fixedValue="true"/>
//    d:DateTimeDomainReference/r:TypeOfObject" defaultValue="ManagedDateTimeRepresentation" fixedValue="true"/>
//
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed"/>
//
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed/d:CodeDomain"/>
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed/d:CodeDomain/r:CodeListReference/r:URN"/>
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed/d:CodeDomain/r:CodeListReference/r:TypeOfObject" defaultValue="CodeList" fixedValue="true"/>
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed/d:ScaleDomainReference/r:TypeOfObject" defaultValue="ManagedScaleRepresentation" fixedValue="true"/>
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed/d:TextDomainReference/r:TypeOfObject" defaultValue="ManagedTextRepresentation" fixedValue="true"/>
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed/d:NumericDomainReference/r:TypeOfObject" defaultValue="ManagedNumericRepresentation" fixedValue="true"/>
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed/d:DateTimeDomainReference/r:TypeOfObject" defaultValue="ManagedDateTimeRepresentation" fixedValue="true"/>
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed/d:MissingValueDomainReference/r:TypeOfObject" defaultValue="MissingCodeRepresentation" fixedValue="true"/>
//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed/d:MissingValueDomainReference/r:TypeOfObject" defaultValue="MissingCodeRepresentation" fixedValue="true"/>



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

    public ResponseDomainFragmentBuilder(ResponseDomain responseDomain) {
        super(responseDomain);
        children = responseDomain.getManagedRepresentationFlatten().stream()
            .map( c -> c.getXmlBuilder() )
            .collect( Collectors.toList() );
//        rdContent = responseDomain.getFlatManagedRepresentation( responseDomain.getManagedRepresentation() ).stream()
//            .collect( Collectors.toMap( AbstractEntity::getId, c->c ));
    }

    @Override
    public void addXmlFragments(Map<String, String> fragments) {
        if (entity.getResponseKind() != ResponseKind.MIXED)
            fragments.putIfAbsent( getUrnId(), getXmlFragment() );
        else {
            entity.getManagedRepresentation().getChildren().forEach( man -> {
                XmlDDIFragmentBuilder<Category> cb = (XmlDDIFragmentBuilder<Category>) man.getXmlBuilder();
                fragments.putIfAbsent(cb.getUrnId() ,man.getXmlBuilder().getXmlFragment()  );
            } );
        }
        for(AbstractXmlBuilder child : children) {
            child.addXmlFragments( fragments );
        }
    }

    @Override
    public String getXmlEntityRef() {
        if (entity.getResponseKind() == ResponseKind.MIXED) {
            return String.format( xmlMixedRef,  getInMixedRef() );
        }
        return String.format( xmlRef,  entity.getResponseKind().getDDIName(), getXmlURN(entity) );
    }

    @Override
    protected <S extends AbstractEntityAudit> String getXmlHeader(S instance){
        return String.format(xmlHeader, entity.getResponseKind().getDDIName(), entity.getModified(), getXmlURN(entity)+ getXmlUserId(entity)+ getXmlRationale(entity)+ getXmlBasedOn(entity));
    }

    @Override
    protected <S extends AbstractEntityAudit> String getXmlFooter(S instance){
        return String.format( xmlFooter, instance.getClass().getSimpleName() );
    }

    @Override
    public String getXmlFragment() {
        return String.format( xmlManaged,
            entity.getName(),
            entity.getDescription(),
            children.stream()
                .map( q -> q.getXmlEntityRef() )
                .collect( Collectors.joining() ),

            entity.getXmlLang());
    }

    protected String getXmlManagedRepresentation() {
        return "";
    }


    private String getInMixedRef() {
        return entity.getManagedRepresentation().getChildren().stream()
            .map( ref -> String.format( xmlInMixed, ref.getXmlBuilder().getXmlEntityRef()  ) )
            .collect( Collectors.joining());
    }

}
