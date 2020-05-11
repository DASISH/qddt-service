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

    private final String xmlBody = 
        "<%1$s missingValue=\"\" blankIsMissingValue=\"false\" classificationLevel=%2$s >\n" +
            "Content: \n" +
        "</%1$s>";

        private final String xmlConcept =
        "<d:Concept>\n" +
            "\t%1$s"+
            "\t%2$s"+
            "\t%3$s"+
            "\t<r:Name maxLength=\"250\">\n" +
            "\t\t<r:Content xml:lang=\"%8$s\">%4$s</r:Content>\n" +
            "\t</r:Name>\n" +
            "\t<r:Description maxLength=\"10000\">\n" +
            "\t\t<r:Content xml:lang=\"%8$s\" isPlainText=\"false\">%5$s</r:Content>\n" +
            "\t</r:Description>\n" +
            "\t%6$s" +
            "\t%7$s" +
        "</d:Concept>\n";        

//    d:StructuredMixedResponseDomain/d:ResponseDomainInMixed"/>

    private final String xmlMixedRef =
        "\t<d:StructuredMixedResponseDomain>\n" +
            "%1$s" +
        "\t</d:StructuredMixedResponseDomain>\n";

    private final String xmlInMixed =
        "\t\t<d:ResponseDomainInMixed>\n" +
            "%1$s" +
        "\t\t</d:ResponseDomainInMixed>\n";


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

        List<Category> children =  entity.getResponseKind() == ResponseKind.MIXED ? 
            responseDomain.getManagedRepresentation().getChildren() : 
            List.of(responseDomain.getManagedRepresentation());

        manRep = children.stream()
            .map( FragmentBuilderManageRep::new )
            .collect( Collectors.toList() );
    }

    @Override
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId(), getXmlFragment() );
        manRep.forEach(c -> c.addXmlFragments(fragments));
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
        return String.format( xmlConcept,
            getXmlURN(entity),
            getXmlRationale(entity),
            getXmlBasedOn(entity),
            entity.getName(),
            entity.getDescription(),
            questions.stream()
                .map( q -> q.getXmlEntityRef() )
                .collect( Collectors.joining() ),
            children.stream()
                .map( c -> c.getXmlEntityRef() )
                .collect( Collectors.joining()),
            entity.getXmlLang());

        return String.format( xmlManaged,
            entity.getClass().getSimpleName(),
            entity.getModified(),
            entity.getName(),
            entity.getDescription(),
            entity.getXmlLang(), "5", "6", "7", "8", "9", "10",
            manRep.stream()
                .map( q -> q.getXmlEntityRef() )
                .collect( Collectors.joining() )
            );
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
