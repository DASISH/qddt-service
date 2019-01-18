package no.nsd.qddt.domain.questionitem;

import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class QuestionItemFragmentBuilder extends XmlDDIFragmentBuilder<QuestionItem> {
    private final String xmlQuestionItem =
        "<d:QuestionItem>\n" +
            "\t%1$s" +
            "\t%2$s" +
            "\t%3$s" +
            "\t<QuestionItemName>\n" +
            "\t\t<r:String>%4$s</r:String>\n" +
            "\t</QuestionItemName>\n" +
            "\t<QuestionIntent>\n" +
            "\t\t<r:Content xml:lang=\"%7$s\">%5$s</r:Content>\n" +
            "\t</QuestionIntent>\n" +
            "\t<QuestionText>\n" +
            "\t\t<r:Content xml:lang=\"%7$s\" isPlainText=\"false\">%6$s</r:Content>\n" +
            "\t</QuestionText>\n" +
            "\t%8$s" +
            "\t%9$s" +
        "</d:QuestionItem>\n";


    private final AbstractXmlBuilder responseBuilder;

    public QuestionItemFragmentBuilder(QuestionItem questionItem) {
        super(questionItem);
        responseBuilder = questionItem.getResponseDomain().getXmlBuilder();
    }

    protected String getConceptRef() {
        return entity.getConceptRefs().stream()
            .map(cr ->{
                String urn = String.format(xmlURN1, cr.getAgency(),cr.getId(),cr.getVersion().toDDIXml());
                return String.format( xmlRef, xmlTagPreFix, "Concept", urn);
                })
            .collect( Collectors.joining());
    }

    @Override
    public void setEntityBody(Map<UUID, String> fragments) {
        fragments.putIfAbsent( entity.getId(), getXmlBody() );
        responseBuilder.setEntityBody( fragments );
    }

    private String getXmlBody() {
        return String.format( xmlQuestionItem,
            getId(),
            getRationale(),
            getBasedOn(),
            entity.getName(),
            entity.getIntent(),
            entity.getQuestion(),
            entity.getXmlLang(),
            responseBuilder.getEntityRef(),
            getConceptRef());
    }
}
