package no.nsd.qddt.domain.topicgroup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Archivable;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.Authorable;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.othermaterial.OtherMaterialT;
import no.nsd.qddt.domain.pdf.PdfReport;
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

    private String abstractDescription;
    private Set<Author> authors = new HashSet<>();
    private Set<OtherMaterialT> otherMaterials = new HashSet<>();
    private Set<TopicGroupQuestionItem> topicQuestionItems = new HashSet<>(0);
    private Set<Concept> concepts = new HashSet<>(0);
    @Column(name = "study_id", insertable = false, updatable = false)
    private UUID studyId;
    private Study study;
    private boolean isArchived;


    public TopicGroup() {
    }

    @Column(name = "description", length = 10000)
    public String getAbstractDescription() {
        return abstractDescription;
    }
    public void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }


    @Override
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH})
    @JoinTable(name = "TOPIC_AUTHORS",
        joinColumns = {@JoinColumn(name ="topic_id")},
        inverseJoinColumns = {@JoinColumn(name = "author_id")})
    public Set<Author> getAuthors() {
        return this.authors;
    }
    @Override
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
    @Override
    public void addAuthor(Author user) {
        authors.add(user);
    }


    @OneToMany(mappedBy = "parent" ,fetch = FetchType.LAZY, cascade =CascadeType.REMOVE)
    public Set<OtherMaterialT> getOtherMaterials() {
        try {
            return otherMaterials;
        } catch (Exception ex ){
            LOG.error( ex.getMessage() );
            return null;
        }
    }
    public void setOtherMaterials(Set<OtherMaterialT> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }
    public OtherMaterialT addOtherMaterial(OtherMaterialT otherMaterial) {
        otherMaterial.setParent( this );
        return  otherMaterial;
    }


    @Column(name="study_id", insertable = false ,updatable = false)
    public UUID getStudyId() {
        return studyId;
    }
    public void setStudyId(UUID studyId) {
        this.studyId = studyId;
    }

    @JsonBackReference(value = "studyRef")
    @ManyToOne()
    @JoinColumn(name="study_id",updatable = false)
    public Study getStudy() {
        return study;
    }
    public void setStudy(Study study) {
        this.study = study;
    }

    @Transient
    @JsonIgnore
    public void setParent(Study newParent) {
        setField( "study", newParent );
    }
    @Transient
    @JsonIgnore
    public void setParentU(UUID studyId) {
        setField("studyId",studyId );
    }


    @JsonIgnore
    @OrderBy(value = "name asc")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "topicGroup", cascade = { CascadeType.MERGE, CascadeType.REMOVE})
    public Set<Concept> getConcepts() {
        return concepts;
    }
    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }
    public Concept addConcept(Concept concept){
        concept.setTopicGroup(this);
        setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("Concept ["+ concept.getName() +"] added");
        return concept;
    }



    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.MERGE }, mappedBy = "topicGroup")
    public Set<TopicGroupQuestionItem> getTopicQuestionItems() {
        return topicQuestionItems;
    }
    public void setTopicQuestionItems(Set<TopicGroupQuestionItem> topicQuestionItems) {
        this.topicQuestionItems = topicQuestionItems;
    }
    public void addTopicQuestionItem(TopicGroupQuestionItem topicQuestionItem) {
        if (this.topicQuestionItems.stream().noneMatch(cqi->topicQuestionItem.getId().equals(cqi.getId()))) {
            topicQuestionItems.add(topicQuestionItem);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation added");
        }
        else
            LOG.debug("ConceptQuestionItem not inserted, match found" );
    }
    public  void removeTopicQuestionItem(UUID qiId){     // no update for QI when removing (it is bound to a revision anyway...).
        int before = topicQuestionItems.size();
        topicQuestionItems.removeIf(q -> q.getQuestionItem().getId().equals(qiId));
        if (before> topicQuestionItems.size()){
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation removed");
        }
    }


    @Override
    @Column(name = "is_archived")
    public boolean isArchived() {
        return isArchived;
    }
    @Override
    public void setArchived(boolean archived) {
        isArchived = archived;

        if (archived) {
            LOG.info( getName() + " isArchived(" + getConcepts().size() +")" );
            setChangeKind(ChangeKind.ARCHIVED);
//            Hibernate.initialize(this.getConcepts());
            for (Concept concept : getConcepts()) {
                if (!concept.isArchived())
                    concept.setArchived(archived);
            }
        }
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
//                "\"otherMaterials\":" + (otherMaterials == null ? "null" : Arrays.toString(otherMaterials.toArray())) + ", " +
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
        LOG.debug("Topic pre remove");
        getAuthors().clear();
        getOtherMaterials().clear();
    }




}