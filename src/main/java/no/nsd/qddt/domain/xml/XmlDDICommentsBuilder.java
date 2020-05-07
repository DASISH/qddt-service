package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.comment.Comment;

import java.util.Map;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class XmlDDICommentsBuilder extends AbstractXmlBuilder {

    protected final Comment comment;

    public XmlDDICommentsBuilder(Comment comment) {
        this.comment = comment;
    }

      @Override
    public void addXmlFragments(Map<UUID, String> fragments) {

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
