package no.nsd.qddt.domain.instrument;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.elementref.ElementRef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
@Audited
@Entity
public class InstrumentElement  implements Cloneable {

    @JsonIgnore
    @Transient
    private final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    @Id
    @Type(type="pg-uuid")
    @GeneratedValue(generator ="UUID")
    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
    @Column(name ="id", updatable = false, nullable = false)
    private UUID id;


    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "instrument_element_id",updatable = false,insertable = false)
    private InstrumentElement parentReferenceOnly;

    @JsonIgnore
    @Column(name = "_idx" ,insertable = false, updatable = false)
    private Integer index;

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
    @OrderColumn(name="_idx")       // _idx is shared between instrument & InstrumentElement (parent/child)
    @JoinColumn(name = "instrument_element_id")
    @AuditMappedBy(mappedBy = "parentReferenceOnly", positionMappedBy = "index")
    private List<InstrumentElement> sequence = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INSTRUMENT_ELEMENT_PARAMETER",
        joinColumns = @JoinColumn(name = "instrument_element_id", referencedColumnName = "id"))
    public Set<InstrumentParameter> parameters = new HashSet<>();


    @Embedded
    private ElementRef elementRef;

    public InstrumentElement() {
        super();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<InstrumentElement> getSequence() {
        return sequence;
    }
    public void setSequence(List<InstrumentElement> sequence) {
        this.sequence = sequence;
    }

    public Set<InstrumentParameter> getParameters() {
        return parameters;
    }
    public void setParameters(Set<InstrumentParameter> parameters) {
        this.parameters = parameters;
    }

    public ElementRef getElementRef() {
        return elementRef;
    }


    public void setElementRef(ElementRef elementRef) {
        System.out.println("setElementRef " + elementRef);
        if (elementRef.getElement() instanceof ControlConstruct) {
            parameters.addAll(
            ((ControlConstruct) elementRef.getElement()).getOutParameter().stream()
                .map( o -> new InstrumentParameter(o.getName(), o.getReferencedId() ))
                .collect( Collectors.toSet()) );
            parameters.addAll(
                ((ControlConstruct) elementRef.getElement()).getInParameter().stream()
                    .map( p -> new InstrumentParameter(p,null) )
                    .collect( Collectors.toSet()) );
        }
        if (elementRef.getElement() instanceof QuestionConstruct) {
            QuestionConstruct qc = (QuestionConstruct) elementRef.getElement();
            elementRef.setName( qc.getName() + " - " + removeHtmlTags(qc.getQuestionItemRef().getText()));
        }

        this.elementRef = elementRef;
    }


    private String removeHtmlTags(String string) {
        Matcher m = REMOVE_TAGS.matcher(string);
        return m.replaceAll("");
    }

    public InstrumentElement clone(){
        InstrumentElement clone = new InstrumentElement();
        clone.setElementRef( this.elementRef.clone() );
        clone.setParameters( this.getParameters().stream()
            .map(p ->  new InstrumentParameter(p.getName(), p.getReferencedId()) )
            .collect( Collectors.toSet() ) );
        clone.setSequence( this.sequence.stream()
            .map( InstrumentElement::clone )
            .collect( Collectors.toList() ) );
        return clone;
    }
}
