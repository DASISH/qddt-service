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
    public void addFragments(Map<UUID, String> fragments) {

    }

    @Override
    public String getEntityRef() {
        return null;
    }

    @Override
    public String getXmlFragment() {
        return null;
    }

}
