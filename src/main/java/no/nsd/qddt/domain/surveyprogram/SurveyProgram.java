package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.IAuthor;
import no.nsd.qddt.domain.classes.interfaces.IArchived;
import no.nsd.qddt.domain.classes.pdf.PdfReport;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import org.hibernate.Hibernate;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <ul class="inheritance">
 * <li>A Survey is a root element of this model. Every Survey has atleast one Study and one Instrument.
 *     <ul class="inheritance">
 *         <li>A Study will have of one or more Modules.
 *         <ul class="inheritance">
 *             <li>A Module will have one or more Concepts.
 *             <ul class="inheritance">
 *                 <li>A Concept consist of one or more QuestionItems.
 *                     <ul class="inheritance">
 *                         <li>Every QuestionItem will have a Question.</li>
 *                         <li>Every QuestionItem will have a ResponseDomain.</li>
 *                     </ul>
 *                 </li>
 *              </ul>
 *              </li>
 *          </ul>
 *          </li>
 *      </ul>
 *      <ul class="inheritance"><li>An Instrument will have a ordered list of Questions, all of which are contained in Concepts
 *      belonging to Modules that belongs to the Studies that this Survey has.</li>
 *      </ul>
 * </li>
 * </ul>
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "SURVEY_PROGRAM")
public class SurveyProgram extends AbstractEntityAudit implements IAuthor, IArchived {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "surveyProgram", cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    @OrderColumn(name="survey_idx")
    @AuditMappedBy(mappedBy = "surveyProgram", positionMappedBy = "surveyIdx")
    private List<Study> studies = new ArrayList<>();

    @Column(length = 20000)
    private String description;

    @Column(name = "agency_idx", insertable = false, updatable = false)
    private Integer agencyIdx;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "name ASC,email DESC")
    @JoinTable(name = "SURVEY_PROGRAM_AUTHORS",
            joinColumns = {@JoinColumn(name ="survey_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();

    private boolean isArchived;

    public SurveyProgram() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author user){
        authors.add(user);
    }

    public List<Study> getStudies() {
        return studies;
    }

    public void setStudies(List<Study> studies) {
        this.studies = studies;
    }

    public Study addStudy(Study study){
        this.studies.add( study );
        study.setSurveyProgram(this);
        setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("Study ["+ study.getName() +"] added");
        return study;
    }

    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        isArchived = archived;
        if(archived) {
            setChangeKind(ChangeKind.ARCHIVED);
            Hibernate.initialize(this.getStudies());
            for (Study study : getStudies()) {
                if (!study.isArchived())
                    study.setArchived(archived);
            }
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurveyProgram)) return false;
        if (!super.equals(o)) return false;

        SurveyProgram that = (SurveyProgram) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return authors != null ? authors.equals(that.authors) : that.authors == null;
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
        return "SurveyProgram{" +
                "studies=" + studies +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return null;
	}


    @Override
    public void fillDoc(PdfReport pdfReport,String counter) {
        pdfReport.addHeader(this,"Survey");
        pdfReport.addParagraph( this.description );

        if(getComments().size()>0)
            pdfReport.addheader2("Comments");
        pdfReport.addComments(getComments());

        pdfReport.addPadding();

        int i = 0;
        for (Study study : getStudies()) {
            study.fillDoc(pdfReport, counter + ++i );
        }
    }

    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {}



}
