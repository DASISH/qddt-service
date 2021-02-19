package no.nsd.qddt.domain.universe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.classes.pdf.PdfReport;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;

/**
 * @author Stig Norland
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Audited
@Table(name = "UNIVERSE", uniqueConstraints = {@UniqueConstraint(columnNames = {"name","description","agency_id"},name = "UNQ_universe_name")})
public class Universe extends AbstractEntityAudit {

    @Column(name = "description", length = 2000, nullable = false)
    private String description;


    public Universe() {
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (IsNullOrTrimEmpty(getName())) {
            Integer max25 = description.length()>25?25:description.length();
            setName(description.toUpperCase().replace(' ','_').substring(0,max25));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Universe)) return false;
        if (!super.equals(o)) return false;

        Universe that = (Universe) o;

        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Universe{" +
                ", description='" + description + '\'' +
                ", id ='" + getId() + '\'' +
                "}";
    }

    @Override
    public void fillDoc(PdfReport pdfReport,String counter) {}

    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new UniverseFragmentBuilder( this );
	}
}
