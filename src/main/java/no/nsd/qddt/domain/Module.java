package no.nsd.qddt.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "Module")
public class Module extends AbstractEntity{

    private Study study;

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Module module = (Module) o;

        if (study != null ? !study.equals(module.study) : module.study != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (study != null ? study.hashCode() : 0);
        return result;
    }

    @Override
    public String  toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append( "Module{" +
                "study=" + study +
                '}');
        return sb.toString();
    }
}
