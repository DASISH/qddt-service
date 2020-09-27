package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.elementref.AbstractElementRef;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.instrument.pojo.Instrument;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class InstrumentFragmentBuilder extends XmlDDIFragmentBuilder<Instrument> {
    private final String xmlInstrument = "%1$s" + "\t\t\t<d:ConstructName>\n" + "\t\t\t\t<r:String>%3$s</r:String>\n"
            + "\t\t\t</d:ConstructName>\n" + "\t\t\t<r:Label>\n" + "\t\t\t\t<r:Content %2$s>%4$s</r:Content>\n"
            + "\t\t\t</r:Label>\n" + "%5$s" + "%6$s" + "%7$s";

    // r:ConceptReference/r:URN"/>
    // r:ConceptReference/r:TypeOfObject" defaultValue="Concept" fixedValue="true"/>

    protected List<AbstractXmlBuilder> children;

    public InstrumentFragmentBuilder(Instrument entity) {
        super(entity);
        children = new LinkedList<>();
    }

    @Override
    public String getXmlFragment() {
        if (children.size() == 0)
            addChildren();
        return String.format(xmlInstrument, getXmlHeader(entity), getXmlLang(entity), entity.getName(),
                entity.getLabel(), children.stream().map(c -> c.getXmlEntityRef(3)).collect(Collectors.joining()),
                getXmlFooter(entity));
    }

    @Override
    public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {
        super.addXmlFragments(fragments);
        if (children.size() == 0)
            addChildren();
        children.stream().forEach(c -> c.addXmlFragments(fragments));
    }

    private void addChildren() {
        List<AbstractXmlBuilder> collect = entity.getRoot().getChildren().stream()
        .map( seq -> seq.getElement().getXmlBuilder())
        .collect( Collectors.toList());

        children.addAll(collect);
    }


}
