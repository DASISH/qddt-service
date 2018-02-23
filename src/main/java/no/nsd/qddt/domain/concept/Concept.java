package no.nsd.qddt.domain.concept;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Archivable;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.refclasses.TopicRef;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;


/**
 * <ul class="inheritance">
 *     <li>A Concept consist of one or more QuestionItems.</li>
 *     <ul class="inheritance">
 *         <li>Every QuestionItem will have a Question.</li>
 *         <li>Every QuestionItem will have a ResponseDomain.</li>
 *     </ul>
 * </ul>
 * <br>
 * ConceptScheme: Concepts express ideas associated with objects and means of representing the concept
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "CONCEPT")
public class Concept extends AbstractEntityAudit implements Archivable {

    private String label;
    private String description;
    private boolean isArchived;

    private TopicRef topicRef;
    private List<ConceptQuestionItemRev>  conceptQuestionItems = new ArrayList<>();
    private Set<Concept> children = new HashSet<>(0);
    private UUID topicGroupId;
    private TopicGroup topicGroup;
    private Concept parentReferenceOnly;


/*     @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.MERGE ,CascadeType.DETACH }, mappedBy = "concept")
    private Set<ConceptQuestionItem> conceptQuestionItems = new HashSet<>(0); */

    public Concept() {
    }


    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }


    @Column(name = "description", length = 10000)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
            LOG.debug( getName() + " isArchived (" + getChildren().size() +")" );
            setChangeKind(ChangeKind.ARCHIVED);
            for (Concept concept : getChildren()) {
                if (!concept.isArchived())
                    concept.setArchived(archived);
            }
        }
    }


    @Column(name = "topicgroup_id", insertable = false, updatable = false)
    public UUID getTopicGroupId() {
        return topicGroupId;
    }
    public void setTopicGroupId(UUID topicGroupId) {
        this.topicGroupId = topicGroupId;
    }


    @ManyToOne()
    @JsonBackReference(value = "topicGroupRef")
    @JoinColumn(name = "topicgroup_id",updatable = false)
    private TopicGroup getTopicGroup() {
        return topicGroup;
    }
    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }


    @OrderColumn(name="parent_idx")
    @OrderBy("parent_idx ASC")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONCEPT_QUESTION_ITEM",
        joinColumns = @JoinColumn(name="parent_id" , referencedColumnName = "id"))
    public List<ConceptQuestionItemRev> getConceptQuestionItems() {
        return conceptQuestionItems;
    }
    public void setConceptQuestionItems(List<ConceptQuestionItemRev> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }
    public void removeQuestionItem(UUID questionItemId) {       // no update for QI when removing (it is bound to a revision anyway...).
        int before = conceptQuestionItems.size();
        conceptQuestionItems.removeIf(q -> q.getQuestionItem().getId().equals(questionItemId));
        if (before> conceptQuestionItems.size()){
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation removed");
            this.getParents().forEach(p->p.setChangeKind(ChangeKind.UPDATED_CHILD));
        }
	}



/**
    public Set<ConceptQuestionItem> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(Set<ConceptQuestionItem> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }

    public void addConceptQuestionItem(ConceptQuestionItem conceptQuestionItem) {
        if (this.conceptQuestionItems.stream().noneMatch(cqi->conceptQuestionItem.getId().equals(cqi.getId()))) {

            conceptQuestionItems.add(conceptQuestionItem);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation added");
            this.getParents().forEach(p->p.setChangeKind(ChangeKind.UPDATED_CHILD));
        }
        else
            LOG.debug("ConceptQuestionItem not inserted, match found" );
    }


    public void addQuestionItem(QuestionItem questionItem) {
        addConceptQuestionItem(new ConceptQuestionItem(this,questionItem));
    }

    // no update for QI when removing (it is bound to a revision anyway...).
    public  void removeTopicQuestionItem(UUID qiId){
        int before = conceptQuestionItems.size();
        conceptQuestionItems.removeIf(q -> q.getQuestionItem().getId().equals(qiId));
        if (before> conceptQuestionItems.size()){
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation removed");
            this.getParents().forEach(p->p.setChangeKind(ChangeKind.UPDATED_CHILD));
        }
    }
    **/

    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    public Concept getParentReferenceOnly() {
    return parentReferenceOnly;
}
    public void setParentReferenceOnly(Concept parentReferenceOnly) {
        this.parentReferenceOnly = parentReferenceOnly;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REMOVE})
    @OrderBy(value = "name asc")
    @JoinColumn(name = "parent_id")
    @AuditMappedBy(mappedBy = "parentReferenceOnly")
    public Set<Concept> getChildren() {
        return children;
    }
    public void setChildren(Set<Concept> children) {
        this.children = children;
    }
    public void addChildren(Concept concept){
        this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("SubConcept added");
        this.children.add(concept);
        this.getParents().forEach(p->p.setChangeKind(ChangeKind.UPDATED_CHILD));
    }


    @Transient
    @JsonDeserialize
    public TopicRef getTopicRef() {
        if (topicRef == null) {
            TopicGroup topicGroup = findTopicGroup2();
            if (topicGroup == null) {
               topicRef = new TopicRef();
            } else
                topicRef = new TopicRef(topicGroup);
        }

        return topicRef;
    }
    public void setTopicRef(TopicRef topicRef) {
        this.topicRef = topicRef;
    }



    private TopicGroup findTopicGroup2(){
        Concept current = this;
        while(current.getParentReferenceOnly() !=  null){
            current = current.getParentReferenceOnly();
        }
        return current.getTopicGroup();
    }

    @Transient
    @JsonIgnore
    protected List<AbstractEntityAudit> getParents() {
        List<AbstractEntityAudit> retvals = new ArrayList<>( 1 );
        Concept current = this;
        while(current.getParentReferenceOnly() !=  null){
            current = current.getParentReferenceOnly();
            retvals.add( current );
        }
        retvals.add( current.getTopicGroup() );         //this will fail for Concepts that return from clients.
        return retvals;
    }

    @Transient
    @JsonIgnore
    public void setParentT(TopicGroup newParent) {
        setField("topicGroup",newParent );
    }

    @Transient
    @JsonIgnore
    public void setParentU(UUID topicId) {
        setField("topicGroupId",topicId );
    }

    @Transient
    @JsonIgnore
    protected void setParentC(Concept newParent)  {
        setField("parentReferenceOnly",newParent );
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept)) return false;
        if (!super.equals(o)) return false;

        Concept concept = (Concept) o;

        if (label != null ? !label.equals(concept.label) : concept.label != null) return false;
        return !(description != null ? !description.equals(concept.description) : concept.description != null);
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"Concept\":"
                + super.toString()
                + ", \"conceptQuestionItems\":" + conceptQuestionItems
                + ", \"label\":\"" + label + "\""
                + ", \"description\":\"" + description + "\""
                + ", \"children\":" + children
                + "}";
    }


    @Override
    public String toDDIXml(){
        return super.toDDIXml();
    }


    @PreRemove
    private void removeReferencesFromConcept(){
        LOG.debug("Concept pre remove");
//        getConceptQuestionItems().clear();
//        getConceptQuestionItems().forEach(cqi->cqi.getQuestionItem().updateStatusQI(this));
    }


    @Override
    public void fillDoc(PdfReport pdfReport,String counter ) throws IOException {
        try {
            pdfReport.addHeader(this, "Concept " + counter )
                .add(new Paragraph(this.getDescription())
                    .setWidthPercent(80)
                    .setPaddingBottom(15));

            if (getComments().size() > 0) {
                pdfReport.addheader2("Comments");
                pdfReport.addComments(getComments());
                pdfReport.addPadding();
            }

            if (getConceptQuestionItems().size() > 0) {
                pdfReport.addPadding();
                pdfReport.addheader2("QuestionItem(s)");
                for (ConceptQuestionItemRev item : getConceptQuestionItems()) {
                    pdfReport.addheader2(item.getQuestionItem().getName());
                    pdfReport.addParagraph(item.getQuestionItem().getQuestion());
                    if (item.getQuestionItem().getResponseDomain() != null)
                        item.getQuestionItem().getResponseDomain().fillDoc(pdfReport, "");
                    pdfReport.addPadding();
                }
            }

            if (counter.length()>0)
                counter = counter+".";
            int i = 0;
            for (Concept concept : getChildren()) {
                concept.fillDoc(pdfReport, counter + String.valueOf(++i));
                pdfReport.addPadding();
            }

            if (getChildren().size() == 0)
                pdfReport.addPadding();

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw ex;
        }
    }


}
