package no.nsd.qddt.domain.topicgroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.authorable.Authorable;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.study.Study;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * <ul class="inheritance">
 *     <li>A Topic Group (Module) will have one or more Concepts.
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
 *      </li>
 * </ul>
 * <br>
 * A Topic Group (Module) should be a collection of QuestionItems and Concepts that has a theme that is broader than a Concept.
 * All QuestionItems that doesn't belong to a specific Concept, will be collected in a default Concept that
 * every Module should have. This default Concept should not be visualized as a Concept, but as a
 * "Virtual Topic Group (Module)". The reason for this is a simplified data model.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "TOPIC_GROUP")
public class TopicGroup extends AbstractEntityAudit implements Authorable {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_id",updatable = false)
    private Study study;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topicGroup", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OrderBy(value = "name asc")
    private Set<Concept> concepts = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH})
    @JoinTable(name = "TOPIC_AUTHORS",
            joinColumns = {@JoinColumn(name ="topic_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();

    @OneToMany(mappedBy = "owner" ,fetch = FetchType.EAGER, cascade =CascadeType.REMOVE)
    @NotAudited
    private Set<OtherMaterial> otherMaterials = new HashSet<>();


    @Column(name = "description", length = 10000)
    private String abstractDescription;

    @Override
    public void addAuthor(Author user) {
        authors.add(user);
    }

    @Override
    public Set<Author> getAuthors() {
        return this.authors;
    }

    @Override
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }


    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Set<Concept> getConcepts() {
        if  ( concepts == null)
            concepts = new HashSet<>();
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    @Transient
    public Concept addConcept(Concept concept){
        concept.setTopicGroup(this);
        concepts.add(concept);
        setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
        setChangeComment("Concept ["+ concept.getName() +"] added");
        return concept;
    }

    public void removeConcept(Concept concept){
        try {

            concepts = concepts.stream().filter(c->c.equals(concept)== false).collect(Collectors.toSet());
            concept.setTopicGroup(null);
            setChangeKind(ChangeKind.UPDATED_HIERARCY_RELATION);
            setChangeComment("Concept [" + concept.getName() + "] removed");
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Set<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    public TopicQuestions getTopicQuestions(){
        try {
            Concept tq = getConcepts().stream().filter(p -> p.getName() == null).findFirst().orElse(addConcept(new Concept()));
            return new TopicQuestions(tq);
        }catch (Exception ex) {
            System.out.println("getTopicQuestions exception");
            ex.printStackTrace();
            return new TopicQuestions();
        }
    }

    public void setTopicQuestions(Concept concept){
        getConcepts().removeIf(c->c.getId() == concept.getId());
        addConcept(concept);
    }

    @Override
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        super.makeNewCopy(revision);
        getConcepts().forEach(c->c.makeNewCopy(revision));
        getOtherMaterials().forEach(m->m.makeNewCopy(getId()));
        getComments().clear();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicGroup)) return false;
        if (!super.equals(o)) return false;

        TopicGroup that = (TopicGroup) o;

        if (study != null ? !study.equals(that.study) : that.study != null) return false;
        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        if (otherMaterials != null ? !otherMaterials.equals(that.otherMaterials) : that.otherMaterials != null)
            return false;
        if (abstractDescription != null ? !abstractDescription.equals(that.abstractDescription) : that.abstractDescription != null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (abstractDescription != null ? abstractDescription.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopicGroup{" +
                "studyName =" + (study !=null ? study.getName() :"null") +
                ", concepts=" + (concepts !=null ? concepts.size() :"0") +
                ", authors=" + (authors !=null ? authors.size() :"0") +
                ", otherMaterials=" + (otherMaterials !=null ? otherMaterials.size() :"0") +
                ", abstractDescription='" + abstractDescription + '\'' +
                ", name='" + super.getName() + '\'' +
                ", id ='" + super.getId() + '\'' +
                "} ";
    }


    @Override
    public void fillDoc(Document document) throws IOException {

        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        document.add(new Paragraph("Survey Toc:").setFont(font));
        com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List()
                .setSymbolIndent(12)
                .setListSymbol("\u2022")
                .setFont(font);
        list.add(new ListItem(this.getName()));
        document.add(list);
        document.add(new Paragraph(this.getName()));
        document.add(new Paragraph(this.getModifiedBy() + "@" + this.getAgency()));
        document.add(new Paragraph(this.getAbstractDescription()));
        document.add(new Paragraph(this.getTopicQuestions().toString()));
        document.add(new Paragraph(this.getOtherMaterials().toString()));
        document.add(new Paragraph(this.getComments().toString()));

        for (Concept concept : getConcepts()) {
            concept.fillDoc(document);
                                               }

    }
}