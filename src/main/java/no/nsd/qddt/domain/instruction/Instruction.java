package no.nsd.qddt.domain.instruction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
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
@Table(name = "INSTRUCTION", uniqueConstraints = {@UniqueConstraint(columnNames = {"name","description","agency_id"},name = "UNQ_INSTRUCTION_NAME")})
public class Instruction extends AbstractEntityAudit {

    private String description;


    public Instruction() {
    }


    @Column(name = "description", length = 2000,nullable = false)
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
        if (!(o instanceof Instruction)) return false;
        if (!super.equals(o)) return false;

        Instruction that = (Instruction) o;

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
        return "Instruction{" +
                "  id =\'" + getId() + '\'' +
                ", description=\'" + description + "\' }";
    }

    @Override
    public void fillDoc(PdfReport pdfReport,String counter) {
    }

    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return null;
	}

}
