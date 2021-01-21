package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.classes.xml.XmlDDIFragmentBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class PublicationFragmentBuilder extends XmlDDIFragmentBuilder<Publication> {
    private final String xmlPublication=
        "%1$s" +
        "\t\t\t<d:PublicationName>\n" +
        "\t\t\t\t<r:String>%2$s</r:String>\n" +
        "\t\t\t</d:PublicationName>\n" +
        "%3$s" +
        "%4$s";

//    r:ConceptReference/r:URN"/>
//    r:ConceptReference/r:TypeOfObject" defaultValue="Concept" fixedValue="true"/>

    protected List<AbstractXmlBuilder> children;

    public PublicationFragmentBuilder(Publication entity) {
        super(entity);
        children = new LinkedList<>();
    }


    @Override
    public String getXmlFragment() {
        if (children.size() == 0) addChildren( );
        return String.format( xmlPublication,
            getXmlHeader(entity),
            entity.getName(),
            children.stream().map( c -> c.getXmlEntityRef( 3 ) ).collect( Collectors.joining() ),
            getXmlFooter(entity));
    }

    @Override
    public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {
        super.addXmlFragments( fragments );
        if (children.size() == 0) addChildren();
        children.stream().forEach( c -> c.addXmlFragments( fragments ) );
    }

    private void addChildren() {
        children.addAll( entity.getPublicationElements().stream()
            .map( pel -> pel.getElement().getXmlBuilder()  )
            .collect( Collectors.toList()) );
    }


}
