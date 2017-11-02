package no.nsd.qddt.domain.instrument;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstruct.ControlConstructRef;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.refclasses.StudyRef;
import no.nsd.qddt.domain.study.Study;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
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


    @OrderColumn(name="index")
    @OrderBy("instrument_idx ASC")
    @AuditMappedBy(mappedBy = "instrument", positionMappedBy = "index")
    @OneToMany(mappedBy = "instrument",fetch = FetchType.EAGER)
    private List<ControlConstructRef> controlConstructs = new ArrayList<>();

    private String description;

    private String label;

    private String externalInstrumentLocation;

    @Column(name="instrument_kind")
    private String instrumentType;


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

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public List<ControlConstructRef> getControlConstructs() {
        return controlConstructs;
    }

    public void setControlConstructs(List<ControlConstructRef> controlConstructs) {
        this.controlConstructs = controlConstructs;
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
        if (!super.equals(o)) return false;

        Instrument that = (Instrument) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return instrumentType != null ? instrumentType.equals(that.instrumentType) : that.instrumentType == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (instrumentType != null ? instrumentType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "description='" + description + '\'' +
                ", instrumentType='" + instrumentType + '\'' +
                "} " + super.toString();
    }

    @Override
    public void fillDoc(PdfReport pdfReport,String counter) throws IOException {
        pdfReport.getTheDocument().add(new Paragraph()
                .add("Instrument..."));
    }


//    @Override
//    public void makeNewCopy(Integer revision){
//        if (hasRun) return;
//        super.makeNewCopy(revision);
//        getControlConstructs().getChildren().forEach(c->c.makeNewCopy(revision));
//        getComments().clear();
//    }

}
