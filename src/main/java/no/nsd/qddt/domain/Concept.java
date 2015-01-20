package no.nsd.qddt.domain;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 *
 * ConceptScheme: Concepts express ideas associated with objects and means of representing the concept
 */
@Audited
@Entity
@Table(name = "Concept")
public class Concept extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name="module_id")
    private Module module;

    @Column(name = "concept_label")
    private String conceptLabel;

    @Column(name = "concept_description")
    private String conceptDescription;

    @OneToMany(mappedBy="concept", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();


    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getConceptLabel() {
        return conceptLabel;
    }

    public void setConceptLabel(String conceptLabel) {
        this.conceptLabel = conceptLabel;
    }

    public String getConceptDescription() {
        return conceptDescription;
    }

    public void setConceptDescription(String conceptDescription) {
        this.conceptDescription = conceptDescription;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Concept concept = (Concept) o;

        if (comments != null ? !comments.equals(concept.comments) : concept.comments != null) return false;
        if (conceptDescription != null ? !conceptDescription.equals(concept.conceptDescription) : concept.conceptDescription != null)
            return false;
        if (conceptLabel != null ? !conceptLabel.equals(concept.conceptLabel) : concept.conceptLabel != null)
            return false;
        if (module != null ? !module.equals(concept.module) : concept.module != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = module != null ? module.hashCode() : 0;
        result = 31 * result + (conceptLabel != null ? conceptLabel.hashCode() : 0);
        result = 31 * result + (conceptDescription != null ? conceptDescription.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    /* tester ut latmanns to string, skulle funke det? */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "Concept{" +
                "  module=" + module +
                ", conceptLabel='" + conceptLabel + '\'' +
                ", conceptDescription='" + conceptDescription + '\'' +
                ", comments=" + comments) ;
        sb.append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}

