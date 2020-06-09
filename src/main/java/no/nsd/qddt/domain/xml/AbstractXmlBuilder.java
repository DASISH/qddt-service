package no.nsd.qddt.domain.xml;


import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.interfaces.Version;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Stig Norland
 */
public abstract class AbstractXmlBuilder {

    protected final String xmlURN = "<r:URN type=\"URN\" typeOfIdentifier=\"Canonical\">urn:ddi:%1$s:%2$s:%3$s</r:URN>\n";

    protected final String xmlHeader = "\t\t<%1$s:%2$s isUniversallyUnique=\"true\" versionDate=\"%3$s\" isVersionable=\"true\" %4$s >\n" +
                                        "%5$s";

    protected final String xmlFooter = "\t\t</%1$s:%2$s>\n";

    private final String xmlLang = "xml:lang=\"%1$s\"";

    private final String xmlUserId ="\t\t\t<r:UserID typeOfUserID=\"User.Id\">%1$s</r:UserID>\n";

    private final String xmlRationale =
        "\t\t\t<r:VersionResponsibility>%1$s</r:VersionResponsibility>\n" +
        "\t\t\t<r:VersionRationale>\n" +
        "\t\t\t\t<r:RationaleDescription>\n" +
        "\t\t\t\t\t<r:String xml:lang=\"en-GB\">%2$s</r:String>\n" +
        "\t\t\t\t</r:RationaleDescription>\n" +
        "\t\t\t\t<r:RationaleCode>%3$s</r:RationaleCode>\n" +
        "\t\t\t</r:VersionRationale>\n";

    private final String xmlBasedOn =
        "\t\t\t<r:BasedOnObject>\n" +
        "\t\t\t\t<r:BasedOnReference isExternal=\"true\" externalReferenceDefaultURI=\"%3$s\">\n" +
        "\t\t\t\t\t%1$s" +
        "\t\t\t\t\t<r:TypeOfObject>%2$s</r:TypeOfObject>\n"+
        "\t\t\t\t</r:BasedOnReference>\n" +
        "\t\t\t\t<r:BasedOnRationaleDescription><r:String/></r:BasedOnRationaleDescription>\n" +
        "\t\t\t\t<r:BasedOnRationaleCode></r:BasedOnRationaleCode>\n" +
        "\t\t\t</r:BasedOnObject>\n";


//    @Value("${api.rooturl}")
//    private String rooturl;

    public abstract void addXmlFragments( Map<ElementKind,Map<String,String>> fragments);

    public abstract String getXmlEntityRef(int depth);

    public abstract String getXmlFragment();

    protected <S extends AbstractEntityAudit> String getXmlHeader(S instance){
        String prefix = ElementKind.getEnum(instance.getClass().getSimpleName()).getDdiPreFix();
        return String.format(xmlHeader, prefix, 
            instance.getClass().getSimpleName(),
            getInstanceDate(instance),
            "",
            "\t\t\t"+ getXmlURN(instance)+ getXmlUserId(instance)+ getXmlRationale(instance)+ getXmlBasedOn(instance));
    }

    protected <S extends AbstractEntityAudit> String getXmlFooter(S instance){
        String prefix = ElementKind.getEnum(instance.getClass().getSimpleName()).getDdiPreFix();

        return String.format( xmlFooter, prefix, instance.getClass().getSimpleName() );
    }

    protected <S extends AbstractEntityAudit>String getXmlLang(S instance) {
        if (instance.getXmlLang() == null) return "";
        return String.format( xmlLang, instance.getXmlLang() );
    }

    public <S extends AbstractEntityAudit> String getXmlURN(S instance) {
        return  String.format( xmlURN, instance.getAgency().getName(),instance.getId(),instance.getVersion().toDDIXml());
    }

    protected  <S extends AbstractEntity> String getXmlUserId(S instance) {
        return  String.format( xmlUserId,  instance.getModifiedBy().getId());
    }

    protected <S extends AbstractEntityAudit> String getXmlRationale(S instance) {
        return  String.format( xmlRationale,  instance.getModifiedBy().getName() + "@" + instance.getAgency().getName(),
            "["+ instance.getChangeKind().getDescription() + "] ➫ " + instance.getChangeComment(), instance.getChangeKind().name() );
    }

    protected <S extends AbstractEntityAudit> String getXmlBasedOn(S instance) {
        if (instance.getBasedOnObject() == null) return "";
        String uri = "https://qddt.nsd.no/preview/" + instance.getBasedOnObject() + "/" + instance.getBasedOnRevision();
        String urn =  String.format( xmlURN, instance.getAgency().getName(),instance.getBasedOnObject(),new Version(1,0,instance.getBasedOnRevision(),"").toDDIXml());
        return String.format( xmlBasedOn, urn ,instance.getClass().getSimpleName(), uri );
    }

    protected  <S extends AbstractEntityAudit> String getInstanceDate(S instance){
        return instance.getModified().toLocalDateTime()
            .atZone( ZoneId.systemDefault() )
            .withZoneSameInstant( ZoneOffset.UTC )
            .format( DateTimeFormatter.ISO_DATE_TIME );
    }

    protected String getTabs(int depth) {
        return String.join( "", Collections.nCopies( depth, "\t" ) );
    }

    protected String html5toXML(String html5){
        // https://zenodo.org/record/259546
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile("<[^>]*>",
            Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        MatchResult result = p.matcher(html5)
            .appendReplacement(sb,"").toMatchResult();
        return sb.toString();
    }

}
