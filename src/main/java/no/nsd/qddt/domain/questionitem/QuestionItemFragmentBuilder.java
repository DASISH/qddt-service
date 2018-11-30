package no.nsd.qddt.domain.questionitem;

import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class QuestionItemFragmentBuilder extends XmlDDIFragmentBuilder<QuestionItem> {
    private final String xmlQuestionItem =
        "<d:QuestionItem>\n" +
            "\t%1$s\n" +
            "\t%2$s\n" +
            "\t%3$s\n" +
            "\t<r:QuestionItemName maxLength=\"250\">\n" +
            "\t\t<r:Content xml:lang=\"eng-GB\" isTranslated=\"false\" isTranslatable=\"true\" isPlainText=\"true\">%4$s</r:Content>\n" +
            "\t</r:QuestionItemName>\n" +
            "\t<r:QuestionIntent>\n" +
            "\t\t<r:Content xml:lang=\"eng-GB\" isTranslated=\"false\" isTranslatable=\"true\" isPlainText=\"true\">%5$s</r:Content>\n" +
            "\t</r:QuestionIntent>\n" +
            "\t<r:QuestionText>\n" +
            "\t\t<r:Content xml:lang=\"eng-GB\" isTranslated=\"false\" isTranslatable=\"true\" isPlainText=\"true\">%5$s</r:Content>\n" +
            "\t</r:QuestionText>\n" +
            "\t%6$s\n" +
            "</d:QuestionItem>";


    private final AbstractXmlBuilder responseBuilder;

    public QuestionItemFragmentBuilder(QuestionItem questionItem) {
        super(questionItem);
        responseBuilder = questionItem.getResponseDomain().getXmlBuilder();
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
            responseBuilder.getEntityRef());
    }
}
