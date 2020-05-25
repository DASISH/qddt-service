package no.nsd.qddt.domain.instrument;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.envers.Audited;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.parentref.IRefs;
import no.nsd.qddt.domain.parentref.Leaf;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
    @OrderColumn(name = "_idx")
    @JoinColumn(name = "instrument_id")
    private List<InstrumentElement> sequence = new ArrayList<>();

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

    public List<InstrumentElement> getSequence() {
        return sequence;
    }

    public void setSequence(List<InstrumentElement> sequence) {
        this.sequence = sequence;
    }

    @Transient
    public IRefs getStudyRef() {
        try{
            return  new Leaf<Study>(getStudy());
        } catch (Exception ex ) {
            return null;
        }
    }

//    TODO implement outparams....
//    @Transient
//    public List<InstrumentParameter> getOutParameter() {
//        this.sequence.stream().collect( TreeMap::new, TreeMap::putAll,
//            (map1, map2) -> { map1.putAll(map2); return map1; });
//    }


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
    public AbstractXmlBuilder getXmlBuilder() {
        return null;
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
