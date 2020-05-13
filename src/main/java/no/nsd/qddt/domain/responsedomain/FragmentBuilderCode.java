package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryFragmentBuilder;

import java.util.Collections;
import java.util.Map;

/**
 * @author Stig Norland
 */
public class FragmentBuilderCode extends CategoryFragmentBuilder {
    private final String xmlCode =
        "%4$s<l:Code scopeOfUniqueness=\"Agency\" isUniversallyUnique=\"true\" isIdentifiable=\"true\" isDiscrete=\"true\" levelNumber=\"1\"  isTotal=\"false\">\n" +
        "%4$s\t%1$s"+
        "%2$s"+
        "%4$s\t<r:Value xml:space=\"default\">%3$s</r:Value>\n" +
        "%4$s</l:Code>\n";


    public FragmentBuilderCode(Category entity) {
        super( entity );
    }


    @Override
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId() , getXmlFragment() );
    }


    @Override
    public String getXmlEntityRef(int depth) {
        return String.format( xmlCode, getCodeURN(), super.getXmlEntityRef( depth+1 ) , entity.getCode().getCodeValue() , String.join("", Collections.nCopies(depth, "\t")) );
    }

    @Override
    public String getXmlFragment() {
        return super.getXmlFragment();
    }

    private String getCodeURN() {
        return  String.format( xmlURN, entity.getAgency().getName(),entity.getId(),"0000");
    }

}
