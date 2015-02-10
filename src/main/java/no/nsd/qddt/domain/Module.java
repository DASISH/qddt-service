package no.nsd.qddt.domain;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

/**
 * A Module should be a collection of Questions and Concepts that has a theme that is broader than a Concept.
 * All Questions that doesn't belong to a specific Concept, will be collected in a default Concept that
 * every Module should have. This default Concept should not be visualized as a Concept, but as a
 * "Virtual Module". The reason for this is a simplified data model.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "Module")
public class Module extends AbstractEntityAudit {

    @OneToMany
    @JoinColumn(name="author_id")
    private List<User> authors;

    private String authorsAffiliation;

    private String  moduleAbstract;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name="study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "concept_id")
    private Concept concept;

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public List<User> getAuthors() {
        return authors;
    }

    public void setAuthors(List<User> authors) {
        this.authors = authors;
    }

    public String getAuthorsAffiliation() {
        return authorsAffiliation;
    }

    public void setAuthorsAffiliation(String authorsAffiliation) {
        this.authorsAffiliation = authorsAffiliation;
    }

    public String getModuleAbstract() {
        return moduleAbstract;
    }

    public void setModuleAbstract(String moduleAbstract) {
        this.moduleAbstract = moduleAbstract;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
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
    public String toString() {
        return "Module{" +
                "study=" + study +
                super.toString() +
                '}';
    }
}
