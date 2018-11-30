package no.nsd.qddt.domain.xml;


import no.nsd.qddt.domain.AbstractEntity;

import java.util.Map;
import java.util.UUID;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Stig Norland
 */
public abstract class AbstractXmlBuilder<T extends AbstractEntity> {

    protected T entity;

    protected AbstractXmlBuilder(T entity){
        this.entity = entity;
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

    protected abstract String getId();

    public abstract void setEntityBody(Map<UUID,String> fragments);

    public abstract String getEntityRef();







}
