package no.nsd.qddt.domain.study;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
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
@Table(name = "STUDY")
public class Study extends AbstractEntityAudit implements Commentable {

    @ManyToOne
    @JoinColumn(name="survey_id")
    private SurveyProgram surveyProgram;

    @Transient
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy="study", cascade = CascadeType.ALL)
    private Set<Instrument> instruments = new HashSet<>();

    @OneToMany(mappedBy="study", cascade = CascadeType.ALL)
    private Set<TopicGroup> topicGroups = new HashSet<>();

    public Study() {

    }

    public SurveyProgram getSurveyProgram() {
        return surveyProgram;
    }

    public void setSurveyProgram(SurveyProgram surveyProgram) {
        this.surveyProgram = surveyProgram;
    }

    @Override
    public Set<Comment> getComments() {
        return comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public void addComment(Comment comment) {
        comment.setOwnerId(this.getId());
        comments.add(comment);
    }

    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = instruments;
    }

    public Set<TopicGroup> getTopicGroups() {
        return topicGroups;
    }

    public void setTopicGroups(Set<TopicGroup> topicGroups) {
        this.topicGroups = topicGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Study study = (Study) o;
        if (surveyProgram != null ? !surveyProgram.equals(study.surveyProgram) : study.surveyProgram != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (surveyProgram != null ? surveyProgram.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Study{" +
                "surveyProgram=" + surveyProgram +
                ", instruments=" + instruments +
                ", topicGroups=" + topicGroups +
                "} " + super.toString();
    }
}
