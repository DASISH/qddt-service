package no.nsd.qddt.domain.responsedomain;


import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ResponseDomainFragmentBuilder extends XmlDDIFragmentBuilder<ResponseDomain> {

    private final String xmlBody = 
        "\t\t\t<%1$s blankIsMissingValue=\"false\" %2$s >\n" +
        "\t\t\t\t<r:Label>%3$s</r:Label>\n" +
        "\t\t\t\t<r:Description>%4$s</r:Description>\n" +
        "%5$s" +
        "\t\t\t</%1$s>\n";

//        private final String xmlCodeDomain =
//        "<d:%1$s blankIsMissingValue=\"false\" classificationLevel=\"\" >\n" +
//            "\t%2$s"+
//            "\t%3$s"+
//            "\t<r:Name maxLength=\"250\">\n" +
//            "\t\t<r:Content xml:lang=\"%8$s\">%4$s</r:Content>\n" +
//            "\t</r:Name>\n" +
//            "\t<r:Description maxLength=\"10000\">\n" +
//            "\t\t<r:Content xml:lang=\"%8$s\" isPlainText=\"false\">%5$s</r:Content>\n" +
//            "\t</r:Description>\n" +
//            "\t%6$s" +
//            "\t%7$s" +
//        "</d:%1$s>\n";

//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed"/>

    private final String xmlMixedRef =
        "\t\t\t<d:StructuredMixedResponseDomain>\n" +
            "%1$s" +
        "\t\t\t</d:StructuredMixedResponseDomain>\n";

    private final String xmlInMixed =
        "\t\t\t\t<d:ResponseDomainInMixed>\n" +
            "%1$s" +
        "\t\t\t\t</d:ResponseDomainInMixed>\n";


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


    private final List<AbstractXmlBuilder> manRep;

    public ResponseDomainFragmentBuilder(ResponseDomain responseDomain) {
        super(responseDomain);

        List<Category> children =  entity.getResponseKind().equals( ResponseKind.MIXED) ?
            responseDomain.getManagedRepresentation().getChildren() : 
            List.of(responseDomain.getManagedRepresentation());

        manRep = children.stream()
            .map(item ->  new FragmentBuilderManageRep(item, this.entity.getDisplayLayout() ))
            .collect( Collectors.toList() );
    }

    @Override
    public void addXmlFragments(Map<String, String> fragments) {
//        fragments.putIfAbsent( getUrnId(), getXmlFragment() );
        manRep.forEach(c -> c.addXmlFragments(fragments));
    }

    @Override
    public String getXmlEntityRef(int depth) {
        if (entity.getResponseKind() == ResponseKind.MIXED) {
            return String.format( xmlMixedRef,  getInMixedRef() );
        } else if (entity.getResponseKind() == ResponseKind.LIST) {
            return String.format( xmlBody, entity.getResponseKind().getDDIName(), "classificationLevel=\"Ordinal\"",
                    entity.getName(),
                    entity.getDescription(),
                    manRep.stream()
                        .map( q -> q.getXmlEntityRef(4) )
                        .collect( Collectors.joining() )
            );
        }
        return String.format( xmlRef, entity.getResponseKind().getDDIName(), getXmlURN(entity)  , String.join("", Collections.nCopies(depth, "\t")));
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
        return "";
//        return String.format( xmlBody,
//            entity.getResponseKind().getDDIName(),
//            "classificationLevel=\"Ordinal\"",
//            manRep.stream()
//                .map( q -> q.getXmlEntityRef(2) )
//                .collect( Collectors.joining() )
//            );
    }



    private String getInMixedRef() {
        return entity.getManagedRepresentation().getChildren().stream()
            .map( ref -> String.format( xmlInMixed, ref.getXmlBuilder().getXmlEntityRef(5)  ) )
            .collect( Collectors.joining( ));
    }

}
