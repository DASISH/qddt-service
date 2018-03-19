package no.nsd.qddt.domain.report;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "REPORT")
public class Report implements Serializable {

    private static final long serialVersionUID = -726184734657337877L;

    @Id
    private Long id;

    @Column(name = "updated")
    @Version()
    private Timestamp modified;

    private String name;


//    List<Json> lines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Report() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;

        Report report = (Report) o;

        if (id != null ? !id.equals( report.id ) : report.id != null) return false;
        if (modified != null ? !modified.equals( report.modified ) : report.modified != null) return false;
        return name != null ? name.equals( report.name ) : report.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
            "Report (id=%s, name=%s)", this.id, this.name );
    }


}
