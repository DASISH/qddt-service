package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryFragmentBuilder;

/**
 * @author Stig Norland
 */
public class FragmentBuilderCode extends CategoryFragmentBuilder {
    private final String xmlCode =
        "%4$s<l:Code scopeOfUniqueness=\"Maintainable\" isUniversallyUnique=\"false\" isIdentifiable=\"true\" isDiscrete=\"true\" levelNumber=\"1\"  isTotal=\"false\">\n" +
        "%4$s\t%1$s"+
        "%2$s"+
        "%4$s\t<r:Value xml:space=\"default\">%3$s</r:Value>\n" +
        "%4$s</l:Code>\n";


    public FragmentBuilderCode(Category entity) {
        super( entity );
    }


    @Override
    public String getXmlEntityRef(int depth) {
        return String.format( xmlCode, getCodeURN(), super.getXmlEntityRef( depth+1 ) , entity.getCode().getValue().trim() , getTabs( depth ) );
    }

    @Override
    public String getXmlFragment() {
        return super.getXmlFragment();
    }

    private String getCodeURN() {
        return  String.format( xmlURN, entity.getAgency().getName(),entity.getId(),  entity.getCode().getValue());
    }

}
