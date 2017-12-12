package no.nsd.qddt.domain.topicgroup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Archivable;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.authorable.Authorable;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItem;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
public class TopicGroup extends AbstractEntityAudit implements Authorable,Archivable {

    @Column(name = "description", length = 10000)
    private String abstractDescription;

//    @JsonIgnore
    @JsonBackReference(value = "studyRef")
    @ManyToOne()
    @JoinColumn(name="study_id",updatable = false)
    private Study study;


    @JsonIgnore
    @OrderBy(value = "name asc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topicGroup", cascade = { CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Concept> concepts = new HashSet<>(0);


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.MERGE }, mappedBy = "topicGroup")
    private Set<TopicGroupQuestionItem> topicQuestionItems = new HashSet<>(0);


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH})
    @JoinTable(name = "TOPIC_AUTHORS",
            joinColumns = {@JoinColumn(name ="topic_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();


    @OneToMany(mappedBy = "owner" ,fetch = FetchType.EAGER, cascade =CascadeType.REMOVE)
//    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Set<OtherMaterial> otherMaterials = new HashSet<>();

    private boolean isArchived;

    public TopicGroup() {
    }

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


    public Concept addConcept(Concept concept){
        concept.setTopicGroup(this);
        setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("Concept ["+ concept.getName() +"] added");
        return concept;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
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


    public Set<TopicGroupQuestionItem> getTopicQuestionItems() {
        return topicQuestionItems;
    }

    public void setTopicQuestionItems(Set<TopicGroupQuestionItem> topicQuestionItems) {
        this.topicQuestionItems = topicQuestionItems;
    }

    public void addTopicQuestionItem(TopicGroupQuestionItem topicQuestionItem) {
        if (this.topicQuestionItems.stream().noneMatch(cqi->topicQuestionItem.getId().equals(cqi.getId()))) {
// no update for QI when removing (it is bound to a revision anyway...).
//            if (topicQuestionItem.getQuestionItem() != null){
//                topicQuestionItem.getQuestionItem().setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
//                topicQuestionItem.getQuestionItem().setChangeComment("Topic assosiation added");
//            }
            topicQuestionItems.add(topicQuestionItem);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation added");
        }
        else
            System.out.println("ConceptQuestionItem not inserted, match found" );
    }

    public void addQuestionItem(QuestionItem questionItem) {
        addTopicQuestionItem(new TopicGroupQuestionItem(this,questionItem));
    }

    // no update for QI when removing (it is bound to a revision anyway...).
    public  void removeQuestionItem(UUID qiId){
        int before = topicQuestionItems.size();
        topicQuestionItems.removeIf(q -> q.getQuestionItem().getId().equals(qiId));
        if (before> topicQuestionItems.size()){
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation removed");
        }
    }


    @Override
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        super.makeNewCopy(revision);
//        getConcepts().forEach(c->c.makeNewCopy(revision));
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
//        if (concepts != null ? !concepts.equals(that.concepts) : that.concepts != null) return false;
        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        if (otherMaterials != null ? !otherMaterials.equals(that.otherMaterials) : that.otherMaterials != null)
            return false;
        return abstractDescription != null ? abstractDescription.equals(that.abstractDescription) : that.abstractDescription == null;

    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (abstractDescription != null ? abstractDescription.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "{\"_class\":\"TopicGroup\", " +
                super.toString() +
                "\"abstractDescription\":" + (abstractDescription == null ? "null" : "\"" + abstractDescription + "\"") +
                "\"concepts\":" + (getConcepts() == null ? "null" : Arrays.toString(getConcepts().toArray())) + ", " +
                "\"authors\":" + (authors == null ? "null" : Arrays.toString(authors.toArray())) + ", " +
                "\"otherMaterials\":" + (otherMaterials == null ? "null" : Arrays.toString(otherMaterials.toArray())) + ", " +
                "}";
    }


    @Override
    public void fillDoc(PdfReport pdfReport, String counter) throws IOException {

        pdfReport.addHeader(this,"Module " + counter )
        .add(new Paragraph(this.getAbstractDescription())
            .setWidthPercent(80)
            .setPaddingBottom(15));

        if(getComments().size()>0) {
            pdfReport.addheader2("Comments");
            pdfReport.addComments(getComments());
            pdfReport.addPadding();
        }

        if (getTopicQuestionItems().size() > 0) {
            pdfReport.addheader2("QuestionItem(s)");
            for (TopicGroupQuestionItem item : getTopicQuestionItems()) {
                pdfReport.addheader2(item.getQuestionItemLateBound().getName());
                pdfReport.addParagraph(item.getQuestionItemLateBound().getQuestion());
                if (item.getQuestionItemLateBound().getResponseDomain() != null)
                    item.getQuestionItemLateBound().getResponseDomain().fillDoc(pdfReport, "");
                pdfReport.addPadding();
            }
        }

        if (counter.length()>0)
            counter = counter+".";
        int i = 0;
        for (Concept concept : getConcepts()) {
            concept.fillDoc(pdfReport ,counter+ String.valueOf(++i));
        }
    }

    @PreRemove
    public void preRemove(){
        System.out.println("Topic pre remove");
        getAuthors().clear();
        getOtherMaterials().clear();
    }

//    @PostLoad void test() {
//        System.out.println("Postload in class " + getName());
//    }

    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        isArchived = archived;

        if (archived) {
            System.out.println( getName() + " isArchived(" + getConcepts().size() +")" );
            setChangeKind(ChangeKind.ARCHIVED);
//            Hibernate.initialize(this.getConcepts());
            for (Concept concept : getConcepts()) {
                if (!concept.isArchived())
                    concept.setArchived(archived);
            }
        }
    }



}