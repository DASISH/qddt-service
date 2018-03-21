package no.nsd.qddt.domain.instrument;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.refclasses.StudyRef;
import no.nsd.qddt.domain.study.Study;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * You change your meaning by emphasizing different words in your sentence. ex: "I never said she stole my money" has 7 meanings.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "INSTRUMENT")
public class Instrument extends AbstractEntityAudit  {


    @JsonBackReference(value = "studyRef")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_id",updatable = false)
    private Study study;


    @OrderColumn(name="instrument_idx")
    @OrderBy("instrument_idx ASC")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INSTRUMENT_ELEMENT_PARAMETER",
        joinColumns = @JoinColumn(name="instrument_id", referencedColumnName = "id"))
    private List<InstrumentElement> sequence = new ArrayList<>();


    private String description;

    private String label;

    private String externalInstrumentLocation;

    @Column(name="instrument_kind")
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

    public List<InstrumentElement> getSequence() {
        return sequence;
    }

    public void setSequence(List<InstrumentElement> sequence) {
        this.sequence = sequence;
    }

    @Transient
    public StudyRef getStudyRef() {
        try{
            return  new StudyRef(getStudy());
        } catch (Exception ex ) {
            return null;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instrument)) return false;
        if (!super.equals( o )) return false;

        Instrument that = (Instrument) o;

        if (study != null ? !study.equals( that.study ) : that.study != null) return false;
        if (sequence != null ? !sequence.equals( that.sequence ) : that.sequence != null) return false;
        if (description != null ? !description.equals( that.description ) : that.description != null) return false;
        if (label != null ? !label.equals( that.label ) : that.label != null) return false;
        if (externalInstrumentLocation != null ? !externalInstrumentLocation.equals( that.externalInstrumentLocation ) : that.externalInstrumentLocation != null)
            return false;
        return instrumentKind == that.instrumentKind;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (sequence != null ? sequence.size() : 0);
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
            + ", \"sequence\":" + sequence
            + ", \"description\":\"" + description + "\""
            + ", \"label\":\"" + label + "\""
            + ", \"externalInstrumentLocation\":\"" + externalInstrumentLocation + "\""
            + ", \"instrumentKind\":\"" + instrumentKind + "\""
            + "}";
    }

    @Override
    public void fillDoc(PdfReport pdfReport,String counter)  {
        pdfReport.getTheDocument().add(new Paragraph()
                .add("Instrument..."));
    }

    @Override
    protected void beforeUpdate() {

    }

    @Override
    protected void beforeInsert() {

    }

}
