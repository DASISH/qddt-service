package no.nsd.qddt.classes.xml;

import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.classes.elementref.ElementKind;

import java.util.Map;

/**
 * @author Stig Norland
 */
public class XmlDDICommentsBuilder extends AbstractXmlBuilder {

    protected final Comment comment;

    public XmlDDICommentsBuilder(Comment comment) {
        this.comment = comment;
    }


    @Override
    public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {
//        add nothing  ATM
//        fragments.get(ElementKind.getEnum( entity.getClassKind())).putIfAbsent( getUrnId(), getXmlFragment() );
    }

    @Override
    public String getXmlEntityRef(int depth) {
        return null;
    }

    @Override
    public String getXmlFragment() {
        return null;
    }

}
