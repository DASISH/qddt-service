package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.elementref.ConditionRef;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRefImpl;
import no.nsd.qddt.domain.instrument.pojo.Parameter;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@DiscriminatorValue("SEQUENCE_CONSTRUCT")
public class Sequence extends ControlConstruct {

    @Column(length = 3000)
    private String description;

    @OrderColumn(name="sequence_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONTROL_CONSTRUCT_SEQUENCE",
        joinColumns = @JoinColumn(name="sequence_id", referencedColumnName = "id"))
    private List<ElementRefImpl<ControlConstruct>> sequence = new ArrayList<>(0);

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn(name="universe_idx")
    @JoinTable(name = "CONTROL_CONSTRUCT_UNIVERSE",
        joinColumns = {@JoinColumn(name ="question_construct_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "universe_id" , referencedColumnName = "id")})
    private List<Universe> universe =new ArrayList<>(0);

    @Enumerated(EnumType.STRING)
    @Column(name = "CONTROL_CONSTRUCT_SUPER_KIND")
    private SequenceKind sequenceKind;

    @Embedded
    private ConditionRef condition;


    public Sequence() {
        super();
        setName( "root" );
    }

    @PrePersist
    @PreUpdate
    private void setDefaults(){
        if (sequenceKind == null)
            sequenceKind = SequenceKind.SECTION;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ConditionRef getCondition() {
        return condition;
    }

    public void setCondition(ConditionRef condition) {
        this.condition = condition;
    }

    public List<ElementRefImpl<ControlConstruct>> getSequence() {
        if(sequence==null) {
            LOG.info( "sequnece is null" );
        } else if (getCondition()!=null && !sequence.get(0).equals( getCondition() )) {
//            sequence.add( 0,getCondition() );
        }
        return sequence;
    }

    public void setSequence(List<ElementRefImpl<ControlConstruct>> sequence) {
        this.sequence = sequence;
    }

    public SequenceKind getSequenceKind() {
        return sequenceKind;
    }

    public void setSequenceKind(SequenceKind sequenceKind) {
        this.sequenceKind = sequenceKind;
    }

    public List<Universe> getUniverse() {
        return universe;
    }

    public void setUniverse(List<Universe> universe) {
        this.universe = universe;
    }

    @Override
    public Set<Parameter> getParameterIn() {
        Set<Parameter> tmp = getSequence().stream()
            .filter( p -> p.getElement() != null )
            .flatMap( s -> s.getElement().getParameterIn().stream() ).collect( Collectors.toSet()) ;
        tmp.add( new Parameter(this.getName(), "IN") );
        return tmp;
    }

    public Set<Parameter> getParameterOut() {
        return getSequence().stream()
            .filter( p -> p.getElement() != null )
            .flatMap( s -> s.getElement().getParameterOut().stream() )
            .collect( Collectors.toSet()) ;
    }


    @Override
    public void fillDoc(PdfReport pdfReport, String counter)  {
        pdfReport.addHeader(this, "Sequence " + counter);

        pdfReport.addParagraph( this.getDescription() );

        if (getUniverse().size() > 0)
            pdfReport.addheader2("Universe");
        for(Universe uni:getUniverse()){
            pdfReport.addParagraph(uni.getDescription());
        }


        getSequence().forEach( entity -> entity.getElement().fillDoc( pdfReport,counter ) );


        if(getComments().size()>0)
            pdfReport.addheader2("Comments");
        pdfReport.addComments(getComments());

        // pdfReport.addPadding();
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new ControlConstructFragmentBuilder<Sequence>( this ) {
            @Override
            public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {
                super.addXmlFragments( fragments );
                if (children.size() == 0) addChildren();
                children.stream().forEach( c -> c.addXmlFragments( fragments ) );
            }
            @Override
            public String getXmlFragment() {
                if (children.size() == 0) addChildren();
                return super.getXmlFragment();
            }

            private void addChildren() {
                children.addAll( getSequence().stream().map( seq -> seq.getElement().getXmlBuilder() ).collect( Collectors.toList())   );
            }
        };
    }
}
