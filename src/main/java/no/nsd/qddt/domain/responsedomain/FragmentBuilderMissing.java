package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryFragmentBuilder;
import no.nsd.qddt.domain.elementref.ElementKind;

import java.util.Collections;
import java.util.Map;

/**
 * @author Stig Norland
 */
public class FragmentBuilderMissing extends CategoryFragmentBuilder {
    private final String xmlMissing =
        "%4$s<r:MissingCodeRepresentation scopeOfUniqueness=\"Agency\" isUniversallyUnique=\"true\" isIdentifiable=\"true\" isDiscrete=\"true\" levelNumber=\"1\"  isTotal=\"false\">\n" +
            "%4$s\t%1$s"+
            "%2$s"+
            "%4$s</r:MissingCodeRepresentation>\n";

    public FragmentBuilderMissing(Category entity) {
        super(entity);
    }

    @Override
    public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {

    }

    @Override
    public String getXmlEntityRef(int depth) {
        return String.format( xmlMissing, getCodeURN(), super.getXmlEntityRef( depth+1 ) , entity.getCode().getCodeValue() , String.join("", Collections.nCopies(depth, "\t")) );
    }

    @Override
    public String getXmlFragment() {
        return super.getXmlFragment();
    }

    private String getCodeURN() {
        return  String.format( xmlURN, entity.getAgency().getName(),entity.getId(),"0000");
    }

}
