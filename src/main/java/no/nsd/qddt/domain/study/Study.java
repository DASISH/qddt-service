package no.nsd.qddt.domain.study;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Archivable;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.authorable.Authorable;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * <ul class="inheritance">
 *     <li>A Study will have of one or more TopicGroups.
 *     <ul class="inheritance">
 *         <li>A TopicGroup will have one or more Concepts.
 *         <ul class="inheritance">
 *             <li>A Concept consist of one or more QuestionItems.
 *                 <ul class="inheritance">
 *                     <li>Every QuestionItem will have a Question.</li>
 *                 </ul>
 *                 <ul class="inheritance">
 *                     <li>Every QuestionItem will have a ResponseDomain.</li>
 *                 </ul>
 *             </li>
 *          </ul>
 *          </li>
 *      </ul>
 *      </li>
 * </ul>
 * <br>
 * A publication structure for a specific study. Structures identification information, full
 * bibliographic and discovery information, administrative information, all of the reusable
 * delineations used for response domains and variable representations, and TopicGroups covering
 * different points in the lifecycle of the study (DataCollection, LogicalProduct,
 * PhysicalDataProduct, PhysicalInstance, Archive, and DDIProfile).
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "STUDY")
public class Study extends AbstractEntityAudit implements Authorable, Archivable {

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="survey_id",updatable = false)
    private SurveyProgram surveyProgram;

    @Column(length = 10000)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "STUDY_INSTRUMENTS",
            joinColumns = {@JoinColumn(name = "study_id")},
            inverseJoinColumns = {@JoinColumn(name = "instruments_id")})
    private Set<Instrument> instruments = new HashSet<>();

    @OneToMany( cascade = {CascadeType.MERGE ,CascadeType.REMOVE}, mappedBy = "study", fetch = FetchType.LAZY)
    @OrderBy(value = "name ASC")
    private Set<TopicGroup> topicGroups = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "STUDY_AUTHORS",
            joinColumns = {@JoinColumn(name ="study_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();

    private boolean isArchived;


    public Study() {

    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void addAuthor(Author user) {
        //user.addStudy(this); would this work?
        authors.add(user);
        user.addStudy(this);
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public SurveyProgram getSurveyProgram() {
        return surveyProgram;
    }

    public void setSurveyProgram(SurveyProgram surveyProgram) {
        this.surveyProgram = surveyProgram;
    }



    private Set<Instrument> getInstruments() {
        if (instruments == null)
            instruments = new HashSet<>();
        return instruments;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = instruments;
    }

    public void SetDefaultInstrument() {
        if (instruments == null){
            instruments = new HashSet<>();
        }
        if (getInstruments().size() ==0){
            Instrument instr = new Instrument();
            instr.setName("<Default>");
            instruments.add(instr);
        }
    }

    public Set<TopicGroup> getTopicGroups() {
        return topicGroups;
    }

    public void setTopicGroups(Set<TopicGroup> topicGroups) {
        this.topicGroups = topicGroups;
    }

    @Override
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        super.makeNewCopy(revision);
        getTopicGroups().forEach(t->t.makeNewCopy(revision));
        getInstruments().forEach(i->i.makeNewCopy(revision));
        getComments().clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Study)) return false;
        if (!super.equals(o)) return false;

        Study study = (Study) o;

        if (surveyProgram != null ? !surveyProgram.equals(study.surveyProgram) : study.surveyProgram != null)
            return false;
        if (description != null ? !description.equals(study.description) : study.description != null) return false;
        if (authors != null ? !authors.equals(study.authors) : study.authors != null) return false;
        if (instruments != null ? !instruments.equals(study.instruments) : study.instruments != null) return false;
        return !(topicGroups != null ? !topicGroups.equals(study.topicGroups) : study.topicGroups != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Study{" +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }


    @Override
    public void fillDoc(PdfReport pdfReport) throws IOException {
        Document document =pdfReport.getTheDocument();
        document.add(new Paragraph()
                .add("Name")
                .add(new Tab())
                .add(this.getName()));
        document.add(new Paragraph(this.getDescription()));
        for (TopicGroup topic : getTopicGroups()) {
            topic.fillDoc(pdfReport);
        }
        pdfReport.addFooter(this);
    }

    @PreRemove
    public void remove(){
        System.out.println(" Study pre remove");
        if (this.getSurveyProgram() != null) {
            System.out.println(getSurveyProgram().getName());
            this.getSurveyProgram().getStudies().removeIf(p->p.getId() == this.getId());
        }
        this.getAuthors().clear();
        this.getInstruments().clear();
    }

    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        try {
            isArchived = archived;
            if(archived) {
                setChangeKind(ChangeKind.ARCHIVED);
                Hibernate.initialize(this.getTopicGroups());
                for (TopicGroup topicGroup : getTopicGroups()) {
                    if (!topicGroup.isArchived())
                        topicGroup.setArchived(archived);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
        }
    }

}