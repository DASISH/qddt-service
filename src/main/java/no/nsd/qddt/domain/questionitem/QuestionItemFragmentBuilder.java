package no.nsd.qddt.domain.questionitem;

import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class QuestionItemFragmentBuilder extends XmlDDIFragmentBuilder<QuestionItem> {
    private final String xmlQuestionItem =
        "%1$s" +
            "\t<QuestionItemName>\n" +
            "\t\t<r:String>%2$s</r:String>\n" +
            "\t</QuestionItemName>\n" +
            "\t<QuestionIntent>\n" +
            "\t\t<r:Content %3$s isPlainText=\"false\">%4$s</r:Content>\n" +
            "\t</QuestionIntent>\n" +
            "\t<QuestionText>\n" +
            "\t\t<r:Content %3$s isPlainText=\"false\">%5$s</r:Content>\n" +
            "\t</QuestionText>\n" +
            "\t%6$s" +
        "%7$s";

//    r:ConceptReference/r:URN"/>
//    r:ConceptReference/r:TypeOfObject" defaultValue="Concept" fixedValue="true"/>


    private final AbstractXmlBuilder responseBuilder;

    public QuestionItemFragmentBuilder(QuestionItem questionItem) {
        super(questionItem);
        responseBuilder = questionItem.getResponseDomainRef().getElement().getXmlBuilder();
    }

    @Override
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId(), getXmlFragment() );
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
            responseBuilder.getXmlEntityRef() + getConceptRef(),
            getXmlFooter(entity));
    }

    protected String getConceptRef() {
        return entity.getConceptRefs().stream()
            .map(cr ->{
                String urn = String.format(xmlURN, cr.getAgency(),cr.getId(),cr.getVersion().toDDIXml());
                return String.format( xmlRef, "", "Concept", urn);
            })
            .collect( Collectors.joining());
    }

}
