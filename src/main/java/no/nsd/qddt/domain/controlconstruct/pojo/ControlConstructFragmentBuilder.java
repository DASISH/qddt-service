package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.classes.elementref.ElementKind;
import no.nsd.qddt.classes.xml.AbstractXmlBuilder;
import no.nsd.qddt.classes.xml.XmlDDIFragmentBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ControlConstructFragmentBuilder<T extends ControlConstruct> extends XmlDDIFragmentBuilder<T> {
    private final String xmlConstruct=
        "%1$s" +
        "\t\t\t<d:ConstructName>\n" +
        "\t\t\t\t<r:String>%3$s</r:String>\n" +
        "\t\t\t</d:ConstructName>\n" +
        "\t\t\t<r:Label>\n" +
        "\t\t\t\t<r:Content %2$s>%4$s</r:Content>\n" +
        "\t\t\t</r:Label>\n" +
        "%5$s" +
        "%6$s" +
        "%7$s";

//    r:ConceptReference/r:URN"/>
//    r:ConceptReference/r:TypeOfObject" defaultValue="Concept" fixedValue="true"/>

    protected List<AbstractXmlBuilder> children;

    public ControlConstructFragmentBuilder(T entity) {
        super(entity);
        children = new LinkedList<>();
    }

    @Override
    public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {
        super.addXmlFragments( fragments );
//        children.stream().forEach( c -> c.addXmlFragments( fragments ) );
    }

    @Override
    public String getXmlFragment() {
        return String.format( xmlConstruct,
            getXmlHeader(entity),
            getXmlLang( entity ),
            entity.getName(),
            entity.getLabel(),
            entity.getOtherMaterials().stream().map( o -> o.toDDIXml(entity,"\t\t\t") ).collect( Collectors.joining()),
            children.stream().map( c -> c.getXmlEntityRef( 3 ) ).collect( Collectors.joining() ),
            getXmlFooter(entity));
    }
//            entity.getOutParameter().stream().map( p -> p.toDDIXml( entity,"\t\t\t" ) ).collect( Collectors.joining())+

}
