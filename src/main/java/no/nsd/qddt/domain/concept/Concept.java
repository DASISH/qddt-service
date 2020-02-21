package no.nsd.qddt.domain.concept;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.IArchived;
import no.nsd.qddt.domain.elementref.AbstractElementRef;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.elementref.IElementRef;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.parentref.TopicRef;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;


/**
 *     <li>A Concept consist of one or more QuestionItems.</li>
 *         <li>Every QuestionItem will have a Question.</li>
 *         <li>Every QuestionItem will have a ResponseDomain.</li>
 * <br>
 * ConceptScheme: Concepts express ideas associated with objects and means of representing the concept
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Audited
@Entity
@Table(name = "CONCEPT")
public class Concept extends AbstractEntityAudit implements IArchived {


    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "concept_id",updatable = false,insertable = false)
    private Concept parentReferenceOnly;


    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @OrderBy(value = "name asc")
    @JoinColumn(name = "concept_id")
    @AuditMappedBy(mappedBy = "parentReferenceOnly")
    private Set<Concept> children = new HashSet<>(0);


    @JsonBackReference(value = "topicGroupRef")
    @ManyToOne()
    @JoinColumn(name = "topicgroup_id",updatable = false)
    private TopicGroup topicGroup;

    @Column(name = "topicgroup_id", insertable = false, updatable = false)
    private UUID topicGroupId;

    @OrderColumn(name="concept_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONCEPT_QUESTION_ITEM",
        joinColumns = @JoinColumn(name="concept_id", referencedColumnName = "id"))
    private List<ElementRef<QuestionItem>>  conceptQuestionItems = new ArrayList<>();

    private String label;

    @Column(name = "description", length = 10000)
    private String description;

    @Transient
    @JsonDeserialize
    private TopicRef topicRef;

    private boolean isArchived;


    public Concept() {
        super();
    }


    private TopicGroup getTopicGroup() {
        return topicGroup;
    }

    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }


    public List<ElementRef<QuestionItem>> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(List<ElementRef<QuestionItem>> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }

    // no update for QI when removing (it is bound to a revision anyway...).
    public void removeQuestionItem(UUID questionItemId, Integer rev) {
        ElementRef toDelete = new ElementRef( ElementKind.QUESTION_ITEM, questionItemId,rev );
        if (conceptQuestionItems.removeIf( q -> q.equals( toDelete ) )) {
            this.setChangeKind( ChangeKind.UPDATED_HIERARCHY_RELATION );
            this.setChangeComment( "QuestionItem assosiation removed" );
            this.getParents().forEach( p -> {
                p.setChangeKind( ChangeKind.UPDATED_CHILD );
                p.setChangeComment( "QuestionItem assosiation removed from child" );
            } );
        }
    }

    public void addQuestionItem(UUID questionItemId, Integer rev) {
        addQuestionItem( new ElementRef( ElementKind.QUESTION_ITEM, questionItemId,rev ) );
    }

    public void addQuestionItem(ElementRef qef) {
        if (this.conceptQuestionItems.stream().noneMatch(cqi->cqi.equals( qef ))) {

            conceptQuestionItems.add(qef);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation added");
            this.getParents().forEach(p->p.setChangeKind(ChangeKind.UPDATED_CHILD));
        }
        else
            LOG.debug("ConceptQuestionItem not inserted, match found" );
    }


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


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
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


    public TopicRef getTopicRef() {
        if (topicRef == null) {
            TopicGroup topicGroup = getParentTopicGroup();
            if (topicGroup == null) {
               topicRef = new TopicRef();
            } else
                topicRef = new TopicRef(topicGroup);
        }

        return topicRef;
    }


    protected Concept getParentRef(){
        return this.parentReferenceOnly;
    }

    protected TopicGroup getParentTopicGroup(){
        Concept current = this;
        while(current.getParentRef() !=  null){
            current = current.getParentRef();
        }
        return current.getTopicGroup();
    }


    private List<AbstractEntityAudit> getParents() {
        List<AbstractEntityAudit> retvals = new ArrayList<>( 1 );
        Concept current = this;
        while(current.getParentRef() !=  null){
            current = current.getParentRef();
            retvals.add( current );
        }
        retvals.add( current.getTopicGroup() );         //this will fail for Concepts that return from clients.
        return retvals; // .stream().filter( f -> f != null ).collect( Collectors.toList());
    }


    protected void setParentC(Concept newParent)  {
        setField("parentReferenceOnly",newParent );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept)) return false;
        if (!super.equals(o)) return false;

        Concept concept = (Concept) o;

        if (!Objects.equals( label, concept.label )) return false;
        return Objects.equals( description, concept.description );
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
        return "{" +
            "\"id\":" + (getId() == null ? "null" : "\"" + getId() +"\"" ) + ", " +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
            "\"label\":" + (label == null ? "null" : "\"" + label + "\"") + ", " +
            "\"description\":" + (description == null ? "null" : "\"" + description + "\"") + ", " +
            "\"topicGroupId\":" + (topicGroupId == null ? "null" : topicGroupId) + ", " +
            "\"conceptQuestionItems\":" + (conceptQuestionItems == null ? "null" : Arrays.toString( conceptQuestionItems.toArray() )) + ", " +
            "\"children\":" + (children == null ? "null" : Arrays.toString( children.toArray() )) + ", " +
            "\"modified\":" + (getModified() == null ? "null" : "\"" + getModified()+ "\"" ) + " , " +
            "\"modifiedBy\":" + (getModifiedBy() == null ? "null" : getModifiedBy()) +
            "}";
    }


    @Override
    @JsonIgnore
    public AbstractXmlBuilder getXmlBuilder() {
        return new ConceptFragmentBuilder(this);
    }



    @Override
    public void fillDoc(PdfReport pdfReport,String counter ) {
        try {
            pdfReport.addHeader(this, "Concept " + counter );
            pdfReport.addParagraph( this.description );

            if (getComments().size() > 0) {
                pdfReport.addheader2("Comments");
                pdfReport.addComments(getComments());
                // pdfReport.addPadding();
            }

            if (getConceptQuestionItems().size() > 0) {
                // pdfReport.addPadding();
                pdfReport.addheader2("QuestionItem(s)");
                getConceptQuestionItems().stream()
                    .map( cqi ->  {
                        if (cqi.getElement() == null){
                            LOG.info( cqi.toString() );
                            return null;
                        }
                        return cqi.getElement();
                    } )
                    .forEach( item -> {
                        pdfReport.addheader2( item.getName(), String.format( "Version %s", item.getVersion() ) );
                        pdfReport.addParagraph( item.getQuestion() );
                        if (item.getResponseDomainRef().getElement() != null)
                            item.getResponseDomainRef().getElement().fillDoc( pdfReport, "" );
                });
            }

            pdfReport.addPadding();

            if (counter.length()>0)
                counter = counter+".";
                int i = 0;
            for (Concept concept : getChildren()) {
                concept.fillDoc(pdfReport, counter + ++i );
            }

            if (getChildren().size() == 0)
                 pdfReport.addPadding();

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {}


}
