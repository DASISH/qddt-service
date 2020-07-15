package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryFragmentBuilder;

import java.util.Collections;

/**
 * @author Stig Norland
 */
public class FragmentBuilderAnchor extends CategoryFragmentBuilder {
    private final String xmlAnchor =
        "%3$s<r:Anchor value=\"%1$s\">\n" +
        "%2$s"+
        "%3$s</r:Anchor>\n";

    public FragmentBuilderAnchor(Category entity) {
        super( entity );
    }


    @Override
    public String getXmlEntityRef(int depth) {
        return String.format( xmlAnchor, entity.getCode().getValue(), super.getXmlEntityRef( depth +1 ) , String.join("", Collections.nCopies(depth, "\t")));
    }

}
