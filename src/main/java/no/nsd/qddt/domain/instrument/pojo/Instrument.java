package no.nsd.qddt.domain.instrument.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.classes.elementref.ParentRef;
import no.nsd.qddt.domain.instrument.InstrumentFragmentBuilder;
import no.nsd.qddt.domain.classes.pdf.PdfReport;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * You change your meaning by emphasizing different words in your sentence. ex:
 * "I never said she stole my money" has 7 meanings.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "INSTRUMENT")
public class Instrument extends AbstractEntityAudit {

    @JsonBackReference(value = "studyRef")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", updatable = false)
    private Study study;

    @OneToOne(fetch = FetchType.EAGER, cascade =  {CascadeType.REMOVE, CascadeType.MERGE})
    private InstrumentNode<?> root;

    private String description;

    private String label;

    private String externalInstrumentLocation;

    @Column(name = "instrument_kind")
    @Enumerated(EnumType.STRING)
    private InstrumentKind instrumentKind;

    public Instrument() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (getName() == null)
            setName(label.toUpperCase());
        this.label = label;
    }

    public String getExternalInstrumentLocation() {
        return externalInstrumentLocation;
    }

    public void setExternalInstrumentLocation(String externalInstrumentLocation) {
        this.externalInstrumentLocation = externalInstrumentLocation;
    }

    public InstrumentKind getInstrumentKind() {
        return instrumentKind;
    }

    public void setInstrumentKind(InstrumentKind instrumentKind) {
        this.instrumentKind = instrumentKind;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public InstrumentNode<?> getRoot() {
        return root;
    }

    public void setRoot(InstrumentNode<?> root) {
        this.root = root;
    }


    @Transient
    public ParentRef<Study> getParentRef() {
        try{
            return new ParentRef<>( getStudy() );
        } catch (Exception ex ) {
            return null;
        }
    }

////    TODO implement outparams....
//    @Transient
//    @JsonSerialize
//    public Map<String,OutParameter> getOutParameter() {
//        this.sequence.stream()
//            .flatMap( p -> p.getOutParameters().stream() )
//            .collect( Collectors.toMap(OutParameter::getId, op -> op ) )
//            .values()
//            .collect( TreeMap::new, TreeMap::putAll, (map1, map2) -> { map1.putAll(map2); return map1; });
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instrument)) return false;
        if (!super.equals( o )) return false;

        Instrument that = (Instrument) o;

        if (study != null ? !study.equals( that.study ) : that.study != null) return false;
//        if (sequence != null ? !sequence.equals( that.sequence ) : that.sequence != null) return false;
        if (description != null ? !description.equals( that.description ) : that.description != null) return false;
        if (label != null ? !label.equals( that.label ) : that.label != null) return false;
        if (externalInstrumentLocation != null ? !externalInstrumentLocation.equals( that.externalInstrumentLocation ) : that.externalInstrumentLocation != null)
            return false;
        return instrumentKind == that.instrumentKind;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (externalInstrumentLocation != null ? externalInstrumentLocation.hashCode() : 0);
        result = 31 * result + (instrumentKind != null ? instrumentKind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"Instrument\":"
            + super.toString()
            + ", \"study\":" + study
            + ", \"description\":\"" + description + "\""
            + ", \"label\":\"" + label + "\""
            + ", \"externalInstrumentLocation\":\"" + externalInstrumentLocation + "\""
            + ", \"instrumentKind\":\"" + instrumentKind + "\""
            + "}";
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new InstrumentFragmentBuilder(this  );
    }


    @Override
    public void fillDoc(PdfReport pdfReport,String counter)  {
        pdfReport.addParagraph( "Instrument...");
    }

    @Override
    protected void beforeUpdate() {}

    @Override
    protected void beforeInsert() {}


}
