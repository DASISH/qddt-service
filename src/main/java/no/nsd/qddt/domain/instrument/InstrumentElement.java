package no.nsd.qddt.domain.instrument;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Id
    @Type(type="pg-uuid")
    @Column(name = "id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;


    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "instrument_element_id",updatable = false,insertable = false)
    private InstrumentElement parentReferenceOnly;


    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
    @OrderColumn(name="_idx")
    @OrderBy(value = "_idx asc")
    @JoinColumn(name = "instrument_element_id")
    @AuditMappedBy(mappedBy = "parentReferenceOnly")
    private List<InstrumentElement> sequences = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INSTRUMENT_ELEMENT_PARAMETER",
        joinColumns = @JoinColumn(name="instrument_element_id", referencedColumnName = "id"))
    private Set<InstrumentParameter> parameters = new HashSet<>();


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

    public List<InstrumentElement> getSequences() {
        return sequences;
    }
    public void setSequences(List<InstrumentElement> sequence) {
        this.sequences = sequence;
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


    private final Pattern TAGS = Pattern.compile("\\[(.{1,50}?)\\]");
    public void setElementRef(ElementRef elementRef) {
        System.out.println("setElementRef " + elementRef.getName());
        if (elementRef.getElement() instanceof QuestionConstruct) {
            QuestionConstruct qc = (QuestionConstruct) elementRef.getElement();
            parameters.clear();
            String question = qc.getQuestionText();
            Matcher matcher = TAGS.matcher(question);
            if (matcher.find()) {
                for (int i = 0; i < matcher.groupCount() ; i++) {
                    getParameters().add(new InstrumentParameter(matcher.group( i ), null));
                }
            }
            String rd = qc.getQuestionItem().getResponseDomain().toString();
            matcher = TAGS.matcher(rd);
            if (matcher.find()) {
                for (int i = 0; i < matcher.groupCount() ; i++) {
                    getParameters().add(new InstrumentParameter(matcher.group( i ), null));
                }
            }
            elementRef.setName( qc.getName() + " - " + removeHtmlTags(qc.getQuestionText()) );
        }
        this.elementRef = elementRef;
    }


    private final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");
    private String removeHtmlTags(String string) {
        Matcher m = REMOVE_TAGS.matcher(string);
        return m.replaceAll("");
    }

    public InstrumentElement clone(){
        InstrumentElement clone = new InstrumentElement();
        clone.setElementRef( this.elementRef.clone() );
        clone.setParameters( this.getParameters().stream().map( InstrumentParameter::clone ).collect( Collectors.toSet() ) );
        clone.setSequences( this.sequences.stream().map( InstrumentElement::clone ).collect( Collectors.toList() ) );
        return clone;
    }
}
