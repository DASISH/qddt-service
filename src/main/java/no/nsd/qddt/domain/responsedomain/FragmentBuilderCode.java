package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;

/**
 * @author Stig Norland
 */
public class FragmentBuilderCode extends XmlDDIFragmentBuilder<Category> {
    private final String xmlCode =
        "<l:Code scopeOfUniqueness=\"Agency\" isUniversallyUnique=\"true\" isIdentifiable=\"true\" isDiscrete=\"true\" levelNumber=\"1\"  isTotal=\"false\">\n" +
            "\t%1$s"+
            "\t%2$s"+
            "\t<r:Value xml:space=\"default\">%3$s</r:Value>\n" +
        "</l:Code>";


    public FragmentBuilderCode(Category entity) {
        super( entity );
    }


    @Override
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId() , getXmlFragment() );
    }

    @Override
    public String getXmlFragment() {
        return String.format(xmlCode, getCodeURN(), getXmlEntityRef(),   entity.getCode().getCodeValue());
    }

    private String getCodeURN() {
        return  String.format( xmlURN, entity.getAgency().getName(),entity.getId(),"0000");
    }

}
