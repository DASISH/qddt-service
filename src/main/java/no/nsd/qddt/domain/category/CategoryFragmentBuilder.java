package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class CategoryFragmentBuilder extends XmlDDIFragmentBuilder<Category> {
    private final String xmlResponseCategory =
        "<d:Category>\n" +
            "\t%1$s" +
            "\t<UserID typeOfUserID=\"ddi2-category-index\">%2$s</UserID>\n" +
            "\t<r:Label>\n" +
            "\t\t<Content xml:lang=\"%3$s\">%4$s</Content>\n" +
            "\t</Label>\n" +
        "</Category>\n";



    public CategoryFragmentBuilder(Category category) {
        super(category);
    }

    @Override
    public void setEntityBody(Map<UUID, String> fragments) {
        fragments.putIfAbsent( entity.getId(), getXmlBody() );
    }

    private String getXmlBody() {
        return String.format( xmlResponseCategory,
            getId(),
            getRationale(),
            entity.getName()
            );
    }



//    @Override
//    public void setEntityBody(Map<UUID, String> fragments) {
//        fragments.putIfAbsent( entity.getId(), getXmlBody() );
//        for(ConceptFragmentBuilder child : children) {
//            child.setEntityBody( fragments );
//        }
//    }
}
