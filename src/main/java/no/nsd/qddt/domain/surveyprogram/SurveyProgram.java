package no.nsd.qddt.domain.surveyprogram;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Pdfable;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.authorable.Authorable;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.study.Study;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import java.io.ByteArrayOutputStream;



import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
/**
 * <ul class="inheritance">
 * <li>A Survey is a root element of this model. Every Survey has atleast one Study and one Instrument.
 *     <ul class="inheritance">
 *         <li>A Study will have of one or more Modules.</li>
 *         <ul class="inheritance">
 *             <li>A Module will have one or more Concepts.</li>
 *             <ul class="inheritance">
 *                 <li>A Concept consist of one or more QuestionItems.
 *                     <ul class="inheritance">
 *                         <li>Every QuestionItem will have a Question.</li>
 *                     </ul>
 *                     <ul class="inheritance">
 *                         <li>Every QuestionItem will have a ResponseDomain.</li>
 *                     </ul>
 *                 </li>
 *              </ul>
 *          </ul>
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
public class SurveyProgram extends AbstractEntityAudit implements Commentable,Authorable,Pdfable {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "surveyProgram", cascade = CascadeType.ALL)
    @OrderBy(value = "modified ASC")
    private Set<Study> studies = new HashSet<>();

    @Column(length = 10000)
    private String description;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "name ASC")
    @JoinTable(name = "SURVEY_AUTHORS",
            joinColumns = {@JoinColumn(name ="survey_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();

    @OneToMany(mappedBy = "ownerId" ,fetch = FetchType.EAGER)
    @NotAudited
    private Set<Comment> comments = new HashSet<>();

    public SurveyProgram() {

    }

//    @PreUpdate
//    private void checkAuthor(){
//        System.out.println("PreUpdate-checkAuthor-> " + getName() );
//        authors.forEach(a->a.addSurvey(this));
//    }

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

    public Set<Study> getStudies() {
        return studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    public Set<Comment> getComments() {
        return comments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurveyProgram)) return false;
        if (!super.equals(o)) return false;

        SurveyProgram that = (SurveyProgram) o;

        if (studies != null ? !studies.equals(that.studies) : that.studies != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        return !(comments != null ? !comments.equals(that.comments) : that.comments != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (studies != null ? studies.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SurveyProgram{" +
                "studies=" + studies +
                ", description='" + description + '\'' +
//                ", authors='" + authors + '\'' +
//                ", comments=" + comments +
                "} " + super.toString();
    }


    @Override
    public ByteArrayOutputStream makePdf() {

        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter( baosPDF));
        Document doc = new Document(pdf);
        fillDoc(doc);
        doc.close();
        return baosPDF;
             }

    @Override
    public void fillDoc(Document document) {
    //    if (document == null)
    //        document = new Document();
              PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
              document.add(new Paragraph("Survay Toc:").setFont(font));
              document.add(new Paragraph("To the top").setFont(font));
                List list = new List()
                .setSymbolIndent(12)
                .setListSymbol("\u2022")
                .setFont(font);
                list.add(new ListItem (this.getName()));

              document.add(list);
              document.add(new Paragraph(this.getName()));
              document.add(new Paragraph(this.getDescription()));
              document.add(new Paragraph(this.getModifiedBy() + "@" + this.getAgency()));
              document.add(new Paragraph(this.getComments().toString()));


        for (Study study : getStudies()) {

           study.fillDoc(document);
        }

       // studies.stream().forEach(s->  document = s.makeDocument(document));
       // return document;
    }
}
