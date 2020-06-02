package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.elementref.ElementRef;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
    private List<ElementRef<ControlConstruct>> sequence = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(name = "CONTROL_CONSTRUCT_SUPER_KIND")
    private SequenceKind sequenceKind;

    public Sequence() {
        super();
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

    public List<ElementRef<ControlConstruct>> getSequence() {
        return sequence;
    }

    public void setSequence(List<ElementRef<ControlConstruct>> sequence) {
        this.sequence = sequence;
    }

    public SequenceKind getSequenceKind() {
        return sequenceKind;
    }

    public void setSequenceKind(SequenceKind sequenceKind) {
        this.sequenceKind = sequenceKind;
    }

    @Override
    public Set<String> getInParameter() {
        return getSequence().stream()
            .filter( p -> p.getElement() != null )
            .flatMap( s -> s.getElement().getInParameter().stream() )
            .collect( Collectors.toSet()) ;
    }

    public Set<OutParameter> getOutParameter() {
        return getSequence().stream()
            .filter( p -> p.getElement() != null )
            .flatMap( s -> s.getElement().getOutParameter().stream() )
            .collect( Collectors.toSet()) ;
    }


}
