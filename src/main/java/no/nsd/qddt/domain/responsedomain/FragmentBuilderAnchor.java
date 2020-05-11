package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryFragmentBuilder;

import java.util.Map;

/**
 * @author Stig Norland
 */
public class FragmentBuilderAnchor extends CategoryFragmentBuilder {
    private final String xmlAnchor =
        "\t\t<r:Anchor value=\"%1$s\">\n" +
        "%2$s"+
        "\t\t</r:Anchor>\n";

    public FragmentBuilderAnchor(Category entity) {
        super( entity );
    }


    @Override
    public void addXmlFragments(Map<String, String> fragments) {
        fragments.putIfAbsent( getUrnId() , getXmlFragment() );
    }

    @Override
    public String getXmlEntityRef(int depth) {
        return String.format( xmlAnchor, entity.getCode().getCodeValue(), super.getXmlEntityRef( 3 ) );
    }
//    public String getXmlFragment() {
//        return String.format( xmlAnchor, entity.getCode().getCodeValue(), getXmlEntityRef(3)
//        );
//    }
//
//    @Override
//    public String getXmlFragment() {
////        if (entity.getCategoryType() == CategoryType.CATEGORY)
//        return String.format( xmlResponseCategory,
//            getXmlHeader( entity ),
//            getXmlLang(entity),
//            entity.getName(),
//            entity.getLabel(),
//            getXmlFooter( entity )
//        );
////        else
////            return "";
//
//    }
}
