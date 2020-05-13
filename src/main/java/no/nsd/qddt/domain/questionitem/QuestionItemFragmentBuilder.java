package no.nsd.qddt.domain.questionitem;

import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class QuestionItemFragmentBuilder extends XmlDDIFragmentBuilder<QuestionItem> {
    private final String xmlQuestionItem =
        "%1$s" +
        "\t\t\t<d:QuestionItemName>\n" +
        "\t\t\t\t<r:String>%2$s</r:String>\n" +
        "\t\t\t</d:QuestionItemName>\n" +
        "\t\t\t<d:QuestionText isStructureRequired=\"false\">\n" +
        "\t\t\t\t<d:LiteralText>\n" +
        "\t\t\t\t\t<d:Text xml:lang=\"nb-NO\" isPlainText=\"false\" xml:space=\"default\">%5$s</d:Text>\n" +
        "\t\t\t\t</d:LiteralText>\n" +
        "\t\t\t</d:QuestionText>\n" +
        "\t\t\t<d:QuestionIntent>\n" +
        "\t\t\t\t<r:Content %3$s isPlainText=\"false\">%4$s</r:Content>\n" +
        "\t\t\t</d:QuestionIntent>\n" +
        "%6$s" +
        "%7$s";

//    r:ConceptReference/r:URN"/>
//    r:ConceptReference/r:TypeOfObject" defaultValue="Concept" fixedValue="true"/>


    private final  XmlDDIFragmentBuilder<ResponseDomain>  responseBuilder;

    public QuestionItemFragmentBuilder(QuestionItem questionItem) {
        super(questionItem);
        responseBuilder = questionItem.getResponseDomainRef().getElement().getXmlBuilder();
    }

    @Override
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId(), getXmlFragment() );
        entity.getConceptRefs().forEach( c -> c.getEntity().getXmlBuilder().addXmlFragments( fragments ) );
        responseBuilder.addXmlFragments( fragments );
    }

    @Override
    public String getXmlFragment() {
        return String.format( xmlQuestionItem,
            getXmlHeader(entity),
            entity.getName(),
            getXmlLang( entity ),
            entity.getIntent(),
            entity.getQuestion(),
            responseBuilder.getXmlEntityRef(3) + getConceptRef(),
            getXmlFooter(entity));
    }

    protected String getConceptRef() {
        return entity.getConceptRefs().stream()
            .map(cr ->{
                String urn = String.format(xmlURN, cr.getAgency(),cr.getId(),cr.getVersion().toDDIXml());
                return String.format( xmlRef, "Concept", urn, "\t\t\t" );
            })
            .collect( Collectors.joining());
    }

}
