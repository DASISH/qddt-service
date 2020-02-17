package no.nsd.qddt.domain.xml;


import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.AbstractEntityAudit;

import java.util.Map;
import java.util.UUID;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Stig Norland
 */
public abstract class AbstractXmlBuilder {

    protected final String xmlURN = "<r:URN type=\"URN\" typeOfIdentifier=\"Canonical\">urn:ddi:%1$s:%2$s:%3$s</r:URN>\n";

    protected final String xmlHeader = "<d:%1$s isUniversallyUnique=\"true\" versionDate=\"%2$s\" isVersionable=\"true\">\n%3$s";

    protected final String xmlFooter = "</d:%1$s>\n";

    private final String xmlLang = "xml:lang=\"%1$s\"";

    private final String xmlUserId ="<r:UserID typeOfUserID=\"User.Id\">%1$s</r:UserID>\n";

    private final String xmlRationale =
        "\t<r:VersionResponsibility>%1$s</r:VersionResponsibility>\n" +
            "\t<r:VersionRationale>\n" +
            "\t\t<r:RationaleDescription>\n" +
            "\t\t\t<r:String xml:lang=\"en\">%2$s</r:String>\n" +
            "\t\t</r:RationaleDescription>\n" +
            "\t\t<r:RationaleCode>%3$s</r:RationaleCode>\n" +
            "\t</r:VersionRationale>\n";

    private final String xmlBasedOn =
        "<r:BasedOnReference>\n" +
            "\t%1$s\n" +
            "\t<r:TypeOfObject>%2$s</r:TypeOfObject>\n"+
            "</r:BasedOnReference>\n";


    public abstract void addFragments(Map<UUID,String> fragments);

    public abstract String getEntityRef();

    public abstract String getXmlFragment();

    protected <S extends AbstractEntityAudit> String getHeader(S instance){
        return String.format(xmlHeader, instance.getClass().getSimpleName(), instance.getModified(), getURN(instance)+ getUserId(instance)+ getRationale(instance)+ getBasedOn(instance));
    }

    protected <S extends AbstractEntityAudit> String getFooter(S instance){
        return String.format( xmlFooter, instance.getClass().getSimpleName() );
    }

    protected <S extends AbstractEntityAudit>String getXmlLang(S instance) {
        if (instance.getXmlLang() == null) return "";
        return String.format( xmlLang, instance.getXmlLang() );
    }

    protected  <S extends AbstractEntityAudit> String getURN(S instance) {
        return  String.format( xmlURN, instance.getAgency().getName(),instance.getId(),instance.getVersion().toDDIXml());
    }

    protected  <S extends AbstractEntity> String getUserId(S instance) {
        return  String.format( xmlUserId,  instance.getModifiedBy().getId());
    }

    protected <S extends AbstractEntityAudit> String getRationale(S instance) {
        return  String.format( xmlRationale,  instance.getModifiedBy().getName() + "@" + instance.getAgency().getName(),
            "["+ instance.getChangeKind().getDescription() + "] ➫ " + instance.getChangeComment(), instance.getChangeKind().name() );
    }

    protected <S extends AbstractEntityAudit> String getBasedOn(S instance) {
        if (instance.getBasedOnObject() == null) return "";
        return String.format( xmlBasedOn, getURN(instance),instance.getClass().getSimpleName() );
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
