package no.nsd.qddt.domain.instrument;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.refclasses.StudyRef;
import no.nsd.qddt.domain.study.Study;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * You change your meaning by emphasizing different words in your sentence. ex: "I never said she stole my money" has 7 meanings.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "INSTRUMENT")
public class Instrument extends AbstractEntityAudit implements Commentable {


    @JsonBackReference(value = "studyRef")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "instruments")
    private Set<Study> studies = new HashSet<>();

    @OneToMany(mappedBy="instrument", cascade = CascadeType.ALL)
    @OrderColumn(name="instrument_idx")
    @OrderBy("instrument_idx ASC")
    // Ordered arrayList doesn't work with Enver FIX
    @AuditMappedBy(mappedBy = "instrument", positionMappedBy = "instrument_idx")
    private List<ControlConstruct> controlConstructs = new ArrayList<>();

    private String description;

    @Column(name="instrument_kind")
    private String instrumentType;


    @Transient
    @JsonSerialize
    @JsonDeserialize
    @OneToMany(mappedBy = "ownerId" ,fetch = FetchType.EAGER, cascade =CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public Instrument() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    public List<ControlConstruct> getControlConstructs() {
        return controlConstructs;
    }

    public void setControlConstructs(List<ControlConstruct> controlConstructs) {
        this.controlConstructs = controlConstructs;
    }

    @Transient
    public Set<StudyRef> getStudyRefs() {
        try{
            return  studies.stream().map(s-> new StudyRef(s)).collect(Collectors.toSet());
        } catch (Exception ex ) {
            return null;
        }
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
}
