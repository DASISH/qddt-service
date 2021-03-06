package no.nsd.qddt.domain.concept;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.classes.elementref.ElementRefEmbedded;
import no.nsd.qddt.domain.classes.elementref.ParentRef;
import no.nsd.qddt.domain.classes.interfaces.IArchived;
import no.nsd.qddt.domain.classes.interfaces.IDomainObjectParentRef;
import no.nsd.qddt.domain.classes.pdf.PdfReport;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <ul>
 *     <li>A Concept consist of one or more QuestionItems.</li>
 *         <li>Every QuestionItem will have a Question.</li>
 *         <li>Every QuestionItem will have a ResponseDomain.</li>
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
public class Concept extends AbstractEntityAudit implements IArchived, IDomainObjectParentRef {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "topicGroupRef")
    @JoinColumn(name="topicgroup_id", nullable = false,updatable = false)
    private TopicGroup topicGroup;

    @Column(name = "topicgroup_id", insertable = false, updatable = false, nullable = false)
    private UUID topicGroupId;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name="concept_id")
    @JsonBackReference(value = "conceptParentRef")
    public Concept parent;

    // in the @OrderColumn annotation on the referencing entity.
    @Column( name = "concept_idx", insertable = false, updatable = false)
    private int conceptIdx;


    @OrderColumn(name="concept_idx")
    @AuditMappedBy(mappedBy = "parent", positionMappedBy = "conceptIdx")
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER,
        orphanRemoval = true, cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE})
    public List<Concept> children= new ArrayList<>(0);


    @OrderColumn(name="concept_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONCEPT_QUESTION_ITEM",
        joinColumns = @JoinColumn(name="concept_id", referencedColumnName = "id"))
    private List<ElementRefEmbedded<QuestionItem>>  conceptQuestionItems = new ArrayList<>(0);

    private String label;

    @Column(name = "description", length = 20000)
    private String description;

    @Transient
    private ParentRef<TopicGroup> parentRef;

    private boolean isArchived;


    public Concept() {
        super();
    }


    public UUID getTopicGroupId() {
        return topicGroupId;
    }

    public int getConceptIdx() {
        return conceptIdx;
    }

    private TopicGroup getTopicGroup() {
        return topicGroup;
    }

    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }

    public List<ElementRefEmbedded<QuestionItem>> getConceptQuestionItems() {
        if (conceptQuestionItems == null) {
            LOG.info( "conceptQuestionItems IS NULL " );
            conceptQuestionItems = new ArrayList<>( 0);
        }
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(List<ElementRefEmbedded<QuestionItem>> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }

    public void removeQuestionItem(UUID id, Integer rev) {
        if (conceptQuestionItems.removeIf( q -> q.getElementId().equals( id) && q.getElementRevision().equals( rev ))) {
            this.setChangeKind( ChangeKind.UPDATED_HIERARCHY_RELATION );
            this.setChangeComment( "QuestionItem assosiation removed" );
            this.getParents().forEach( p -> {
                p.setChangeKind( ChangeKind.UPDATED_CHILD );
                p.setChangeComment( "QuestionItem assosiation removed from child" );
            } );
        }
    }

    public void addQuestionItem(UUID id, Integer rev) {
        addQuestionItem( new ElementRefEmbedded<>( ElementKind.QUESTION_ITEM, id,rev ) );
    }

    public void addQuestionItem(ElementRefEmbedded<QuestionItem> qef) {
        if (this.conceptQuestionItems.stream().noneMatch(cqi->cqi.equals( qef ))) {

            conceptQuestionItems.add(qef);
            this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
            this.setChangeComment("QuestionItem assosiation added");
            this.getParents().forEach(p->p.setChangeKind(ChangeKind.UPDATED_CHILD));
        }
        else
            LOG.debug("ConceptQuestionItem not inserted, match found" );
    }

    public List<Concept> getChildren() {
        if (children == null) {
            LOG.info( "children IS NULL " );
            children = new ArrayList<>( 0);
        }
        return children.stream()
            .filter( Objects::nonNull )
            .collect( Collectors.toList());
    }

    public void setChildren(List<Concept> children) {
        this.children = children;
    }

    public Concept addChildren(Concept concept){
        if(concept == null) return null;
        this.children.add( concept );
        concept.setParent( this);
        setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);
        setChangeComment("SubConcept added");
        getParents().forEach(p->p.setChangeKind(ChangeKind.UPDATED_CHILD));
        return concept;
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
                    concept.setArchived( true );
            }
        }
    }

    @Override
    public ParentRef<TopicGroup> getParentRef() {
        if (parentRef == null )
            parentRef = new ParentRef<>( getParentTopicGroup() );
        return parentRef;
    }

    protected TopicGroup getParentTopicGroup(){
        Concept current = this;
        while(current.getParent() !=  null){
            current = current.getParent();
        }
        return current.getTopicGroup();
    }

    protected Concept getParent(){
        return this.parent;
    }

    public void setParent(Concept parent) {
        this.parent = parent;
    }

    public boolean hasTopicGroup() {
        return (topicGroupId != null);
    }

    private List<AbstractEntityAudit> getParents() {
        List<AbstractEntityAudit> retvals = new ArrayList<>( 1 );
        Concept current = this;
        while(current.getParent() !=  null){
            current = current.getParent();
            retvals.add( current );
        }
        if (current.getTopicGroup()!= null)
            retvals.add( current.getTopicGroup() );         //this will fail for Concepts that return from clients.
        return retvals; // .stream().filter( f -> f != null ).collect( Collectors.toList());
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
            }

            if (getConceptQuestionItems().size() > 0) {
                pdfReport.addheader2("QuestionItem(s)");
                getConceptQuestionItems().stream()
                    .map( cqi -> {
                        if (cqi.getElement() == null) {
                            LOG.info( cqi.toString() );
                            return null;
                        }
                        return cqi.getElement();
                    } )
                    .filter( Objects::nonNull )
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

    @PreRemove
    public void beforeRemove() {}
    //{
    //     LOG.debug(" Concept pre remove");
    //     if (this.getParent() != null) {
    //         this.getParent().getChildren().removeIf(p->p.getId() == this.getId());
    //         AtomicInteger i= new AtomicInteger();
    //     }
    // }

    @Override
    protected void beforeUpdate() {
        if(topicGroupId !=null &&  getTopicGroup() == null) {
            LOG.info( "topicGroup not set " );
        }
    }

    @Override
    protected void beforeInsert() {}

}
