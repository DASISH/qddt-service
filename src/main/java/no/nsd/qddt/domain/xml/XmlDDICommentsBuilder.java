package no.nsd.qddt.domain.xml;

import java.util.Map;

import no.nsd.qddt.domain.comment.Comment;

/**
 * @author Stig Norland
 */
public class XmlDDICommentsBuilder extends AbstractXmlBuilder {

    protected final Comment comment;

    public XmlDDICommentsBuilder(Comment comment) {
        this.comment = comment;
    }

      @Override
    public void addXmlFragments(Map<String, String> fragments) {

    }

    @Override
    public String getXmlEntityRef() {
        return null;
    }

    @Override
    public String getXmlFragment() {
        return null;
    }

}
