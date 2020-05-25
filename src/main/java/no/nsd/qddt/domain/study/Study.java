package no.nsd.qddt.domain.study;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.Hibernate;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.IArchived;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.IAuthor;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.parentref.IParentRef;
import no.nsd.qddt.domain.parentref.IRefs;
import no.nsd.qddt.domain.parentref.Leaf;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.exception.StackTraceFilter;

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
public class Study extends AbstractEntityAudit implements IAuthor, IArchived, IParentRef {

//    @JsonIgnore
    @ManyToOne()
    @JsonBackReference(value = "surveyRef")
    @JoinColumn(name="survey_id",updatable = false)
    private SurveyProgram surveyProgram;

    @Column(name = "survey_id", insertable = false, updatable = false)
    protected UUID surveyId;

    @Column(name = "survey_idx", insertable = false, updatable = false)
    private Integer surveyIdx;

    @Column(length = 10000)
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "study", cascade = { CascadeType.MERGE, CascadeType.DETACH } )
    private Set<Instrument> instruments = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study", cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    @OrderColumn(name="study_idx",nullable = false)
    @AuditMappedBy(mappedBy = "study", positionMappedBy = "studyIdx")
    private List<TopicGroup> topicGroups = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "STUDY_AUTHORS",
            joinColumns = { @JoinColumn(name ="study_id") },
            inverseJoinColumns = { @JoinColumn(name = "author_id") })
    private Set<Author> authors = new HashSet<>();


    @Column(name="is_archived")
    private boolean isArchived;


    public Study() {
        super();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void addAuthor(Author author) {
        authors.add(author);
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

    public Set<Instrument> getInstruments() {
        if (instruments == null)
            instruments = new HashSet<>();
        return instruments;
    }

    public List<TopicGroup> getTopicGroups() {
        return topicGroups;
    }

    public void setTopicGroups(List<TopicGroup> topicGroups) {
        this.topicGroups = topicGroups;
    }

    public TopicGroup addTopicGroup(TopicGroup topicGroup){
        this.topicGroups.add( topicGroup );
        topicGroup.setStudy(this);
        setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("TopicGroup ["+ topicGroup.getName() +"] added");
        return topicGroup;
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

                if (Hibernate.isInitialized(getTopicGroups()))
                    LOG.debug("getTopicGroups isInitialized. ");
                else
                    Hibernate.initialize(getTopicGroups());

                for (TopicGroup topicGroup : getTopicGroups()) {
                    if (!topicGroup.isArchived())
                        topicGroup.setArchived(archived);
                }
            }
        } catch (Exception ex) {
            LOG.error("setArchived",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);

        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Study)) return false;
        if (!super.equals(o)) return false;

        Study study = (Study) o;

        if (!Objects.equals( surveyProgram, study.surveyProgram ))
            return false;
        if (!Objects.equals( description, study.description )) return false;
        if (!Objects.equals( authors, study.authors )) return false;
        if (!Objects.equals( instruments, study.instruments )) return false;
        return !(!Objects.equals( topicGroups, study.topicGroups ));

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
    public AbstractXmlBuilder getXmlBuilder() {
        return null;
	}


    @Override
    public void fillDoc(PdfReport pdfReport, String counter) {
        pdfReport.addHeader(this,"Study " + counter );
        pdfReport.addParagraph( this.description );

        if(getComments().size()>0)
            pdfReport.addheader2("Comments");
        pdfReport.addComments(getComments());
        pdfReport.addPadding();

        if (counter.length()>0)
            counter = counter+".";
        int i=0;
        for (TopicGroup topic : getTopicGroups()) {
            topic.fillDoc(pdfReport, counter+ String.valueOf(++i));
        }
    }

    @PreRemove
    public void remove(){
        LOG.debug(" Study pre remove");
        if (this.getSurveyProgram() != null) {
            LOG.debug(getSurveyProgram().getName());
            this.getSurveyProgram().getStudies().removeIf(p->p.getId() == this.getId());
        }
        this.getAuthors().clear();
        this.getInstruments().clear();
    }

    @Override
    protected void beforeUpdate() {
        LOG.info("Study beforeUpdate");
        if (this.surveyIdx == null) {
            LOG.info( "Setting surveyIdx" );
            this.surveyIdx = this.getSurveyProgram().getStudies().indexOf( this );
        }
    }

    @Override
    protected void beforeInsert() {
        LOG.info("Study beforeInsert");
        if (this.getSurveyProgram() != null && this.surveyIdx == null) {
            LOG.info("Setting surveyIdx");
            this.surveyIdx = this.getSurveyProgram().getStudies().indexOf( this );
        } else {
            LOG.debug("no survey reference, cannot add..");
        }
    }

    @Override
    public IRefs getParentRef() {
        return new Leaf<SurveyProgram>(getSurveyProgram());
    }
}
