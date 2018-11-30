package no.nsd.qddt.domain.xml;

import no.nsd.qddt.domain.comment.Comment;

import java.util.Map;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class XmlDDICommentsBuilder extends AbstractXmlBuilder<Comment> {

    public XmlDDICommentsBuilder(Comment entity) {
        super( entity );
    }

    @Override
    protected String getId() {
        return null;
    }

    @Override
    public void setEntityBody(Map<UUID, String> fragments) {

    }

    @Override
    public String getEntityRef() {
        return null;
    }

}
