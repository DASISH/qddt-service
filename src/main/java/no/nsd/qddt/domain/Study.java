package no.nsd.qddt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <ul class="inheritance">
 *     <li>A Study will have of one or more Modules.
 *     <ul class="inheritance">
 *         <li>A Module will have one or more Concepts.</li>
 *         <ul class="inheritance">
 *             <li>A Concept consist of one or more Questions.</li>
 *             <ul class="inheritance">
 *                 <li>Every Question will have a ResponseDomain.</li>
 *             </ul>
 *          </ul>
 *      </ul>
 *      </li>
 * </ul>
 * </br>
 * A publication structure for a specific study. Structures identification information, full
 * bibliographic and discovery information, administrative information, all of the reusable
 * delineations used for response domains and variable representations, and modules covering
 * different points in the lifecycle of the study (DataCollection, LogicalProduct,
 * PhysicalDataProduct, PhysicalInstance, Archive, and DDIProfile).
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "study")
public class Study extends AbstractEntityAudit {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="survey_id")
    public Survey survey;

    @OneToMany(mappedBy="study", cascade = CascadeType.ALL)
    public Set<Module> modules = new HashSet<>();

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

//    @Override
//    public Agency getAgency() {
//        return getCreatedBy().getAgency();
//    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Study study = (Study) o;
        if (survey != null ? !survey.equals(study.survey) : study.survey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (survey != null ? survey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Study{" +
                "name='" + this.getName() + '\'' +
                ", created=" + this.getCreated() + '\'' +
                ", createdBy=" + this.getCreatedBy() + '\'' +
                ", changeReason='" + this.getChangeReason() + '\'' +
                ", changeComment='" + this.getChangeComment() + '\'' +
                super.toString() +
                '}';
    }
}
