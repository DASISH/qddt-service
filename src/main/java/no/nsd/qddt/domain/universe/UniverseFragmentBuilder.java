package no.nsd.qddt.domain.universe;

import no.nsd.qddt.domain.classes.xml.XmlDDIFragmentBuilder;

/**
 * @author Stig Norland
 */
public class UniverseFragmentBuilder extends XmlDDIFragmentBuilder<Universe> {

    private final String xmlUniverse =
        "%1$s"+
            "\t\t\t<c:UniverseName>\n" +
            "\t\t\t\t<r:String xml:lang=\"%4$s\">%2$s</r:String>\n" +
            "\t\t\t</c:UniverseName>\n"+
            "\t\t\t<r:Description>\n" +
            "\t\t\t\t<r:Content xml:lang=\"%4$s\" isPlainText=\"false\"><![CDATA[%3$s]]></r:Content>\n" +
            "\t\t\t</r:Description>\n" +
            "\t\t</c:Universe>\n";


    public UniverseFragmentBuilder(Universe entity) {
        super(entity);
    }



    public String getXmlFragment() {
        return String.format( xmlUniverse,
            getXmlHeader(entity),
            entity.getName(),
            entity.getDescription(),
            entity.getXmlLang());
    }

}
