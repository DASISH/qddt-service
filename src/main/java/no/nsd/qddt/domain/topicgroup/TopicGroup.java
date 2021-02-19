package no.nsd.qddt.domain.topicgroup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.IAuthor;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.classes.elementref.ElementRefEmbedded;
import no.nsd.qddt.domain.classes.elementref.ParentRef;
import no.nsd.qddt.domain.classes.interfaces.IArchived;
import no.nsd.qddt.domain.classes.interfaces.IDomainObjectParentRef;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.classes.pdf.PdfReport;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;
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
public class TopicGroup extends AbstractEntityAudit implements IAuthor, IArchived, IDomainObjectParentRef {

    @ManyToOne()
    @JsonBackReference(value = "studyRef")
    @JoinColumn(name="study_id",updatable = false)
    private Study study;

    @Column(name = "study_id", insertable = false, updatable = false)
    protected UUID studyId;

    @Column(name = "study_idx", insertable = false, updatable = false)
    private Integer studyIdx;

    @Column(name = "description", length = 20000)
    private String description;

    @OrderColumn(name="concept_idx")
    @AuditMappedBy(mappedBy = "topicGroup", positionMappedBy = "conceptIdx")
    @OneToMany(mappedBy = "topicGroup", fetch = FetchType.LAZY, targetEntity = Concept.class,
        orphanRemoval = true, cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE})
    public List<Concept> concepts= new ArrayList<>(0);


//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topicGroup", cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
//    @OrderColumn(name="concept_idx")
//    @AuditMappedBy(mappedBy = "topicGroup", positionMappedBy ="conceptIdx")
//    private List<Concept> concepts = new ArrayList<>(0);


    @OrderColumn(name="topicgroup_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TOPIC_GROUP_QUESTION_ITEM",
        joinColumns = @JoinColumn(name="topicgroup_id", referencedColumnName = "id"))
    private List<ElementRefEmbedded<QuestionItem>>  topicQuestionItems = new ArrayList<>(0);


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH})
    @JoinTable(name = "TOPIC_GROUP_AUTHORS",
            joinColumns = {@JoinColumn(name ="topicgroup_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();

    @OrderColumn(name="owner_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TOPIC_GROUP_OTHER_MATERIAL",
        joinColumns = {@JoinColumn(name = "owner_id", referencedColumnName = "id")})
    private List<OtherMaterial> otherMaterials = new ArrayList<>(0);

    @Transient
    private ParentRef<Study> parentRef;

    private boolean isArchived;

    public TopicGroup() {
        super();
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
        if(concept == null) return null;
        this.concepts.add( concept );
        concept.setTopicGroup(this);
        setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("Concept ["+ concept.getName() +"] added");
        return concept;
    }

    public List<Concept> getConcepts() {
        if(concepts == null) return new ArrayList<>(0);
        return concepts.stream()
            .filter( Objects::nonNull )
            .collect( Collectors.toList());
    }

    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public List<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(List<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public void addOtherMaterial(OtherMaterial otherMaterial) {
        if (this.otherMaterials.stream().noneMatch(cqi->cqi.equals( otherMaterial ))) {

            otherMaterials.add(otherMaterial);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            if (StringTool.IsNullOrEmpty( getChangeComment()))
                this.setChangeComment("Other material added");
        }
        else
            LOG.debug("OtherMaterial not inserted, match found" );
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public List<ElementRefEmbedded<QuestionItem>> getTopicQuestionItems() {
        return topicQuestionItems;
    }

    public void setTopicQuestionItems(List<ElementRefEmbedded<QuestionItem>> topicQuestionItems) {
        this.topicQuestionItems = topicQuestionItems;
    }

    // no update for QI when removing (it is bound to a revision anyway...).
    public void removeQuestionItem(UUID questionItemId, Integer rev) {
        ElementRefEmbedded toDelete = new ElementRefEmbedded<QuestionItem>( ElementKind.QUESTION_ITEM, questionItemId,rev );
        if (topicQuestionItems.removeIf( q -> q.equals( toDelete ) )) {
            this.setChangeKind( ChangeKind.UPDATED_HIERARCHY_RELATION );
            this.setChangeComment( "QuestionItem association removed" );
        }
    }

    public void addQuestionItem(UUID questionItemId, Integer rev) {
        addQuestionItem( new ElementRefEmbedded<>( ElementKind.QUESTION_ITEM, questionItemId, rev ) );
    }

    public void addQuestionItem(ElementRefEmbedded<QuestionItem> qef) {
        if (this.topicQuestionItems.stream().noneMatch(cqi->cqi.equals( qef ))) {

            topicQuestionItems.add(qef);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem association added");
        }
        else
            LOG.debug("ConceptQuestionItem not inserted, match found" );
    }

    @Override
    public ParentRef<Study> getParentRef() {
        if (parentRef == null && getStudy() != null )
            parentRef = new ParentRef<>( getStudy() );
        return parentRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicGroup)) return false;
        if (!super.equals(o)) return false;

        TopicGroup that = (TopicGroup) o;

        if (!Objects.equals( study, that.study )) return false;
        if (!Objects.equals( authors, that.authors )) return false;
        if (!Objects.equals( otherMaterials, that.otherMaterials ))
            return false;
        return Objects.equals( description, that.description );

    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "{\"_class\":\"TopicGroup\", " +
                super.toString() +
                "\"description\":" + (description == null ? "null" : "\"" + description + "\"") +
                "\"concepts\":" + (getConcepts() == null ? "null" : Arrays.toString(getConcepts().toArray())) + ", " +
                "\"authors\":" + (authors == null ? "null" : Arrays.toString(authors.toArray())) + ", " +
                "}";
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new TopicGroupFragmentBuilder(this);
    }


    @Override
    public void fillDoc(PdfReport pdfReport, String counter) {

        pdfReport.addHeader(this,"Module " + counter );
        pdfReport.addParagraph( this.description );

        if(getComments().size()>0) {
            pdfReport.addheader2("Comments");
            pdfReport.addComments(getComments());
        }

        if (getTopicQuestionItems().size() > 0) {
            pdfReport.addheader2("QuestionItem(s)");
            for (ElementRefEmbedded<QuestionItem> item : getTopicQuestionItems()) {
                pdfReport.addheader2(item.getElement().getName(), String.format("Version %s",item.getElement().getVersion()));
                pdfReport.addParagraph(item.getElement().getQuestion());
                if (item.getElement().getResponseDomainRef().getElement() != null)
                    item.getElement().getResponseDomainRef().getElement().fillDoc(pdfReport, "");
            }
        }
        pdfReport.addPadding();

        if (counter.length()>0)
            counter = counter+".";
        int i = 0;
        for (Concept concept : getConcepts()) {
            concept.fillDoc(pdfReport ,counter+ ++i );
        }
    }

    @PreRemove
    public void preRemove(){
        LOG.debug("Topic pre remove");
        getAuthors().clear();
        getOtherMaterials().clear();
    }

    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        isArchived = archived;

        if (archived) {
            LOG.info( getName() + " isArchived(" + getConcepts().size() +")" );
            setChangeKind(ChangeKind.ARCHIVED);
            for (Concept concept : getConcepts()) {
                if (!concept.isArchived())
                    concept.setArchived( true );
            }
        }
    }

    @Override
    protected void beforeUpdate() {
        // no extra handling
    }
    @Override
    protected void beforeInsert() {
        // no extra handling
    }

}
