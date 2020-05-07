package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;

/**
 * @author Stig Norland
 */
public class FragmentBuilderAnchor extends XmlDDIFragmentBuilder<Category> {
    private final String xmlAnchor =
        "<r:Anchor value=\"%1$s\">\n" +
        "%2$s"+
        "</r:Anchor>\n";

    public FragmentBuilderAnchor(Category entity) {
        super( entity );
    }


    @Override
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId() , getXmlFragment() );
    }

    @Override
    public String getXmlFragment() {
        return String.format( xmlAnchor, entity.getCode().getCodeValue(), getXmlEntityRef()
        );
    }

}
