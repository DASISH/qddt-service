package no.nsd.qddt.domain.concept;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.IArchived;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.parentref.TopicRef;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


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


    @JsonIgnore
    @Column(name = "_idx" ,insertable = false, updatable = false)
    private Integer index;

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
    @OrderColumn(name="_idx")       // _idx is shared between instrument & InstrumentElement (parent/child)
    @JoinColumn(name = "concept_id")
    @AuditMappedBy(mappedBy = "parentReferenceOnly", positionMappedBy = "index")
    private List<Concept> children = new ArrayList<>(0);


    @JsonBackReference(value = "topicGroupRef")
    @ManyToOne()
    @JoinColumn(name = "topicgroup_id",updatable = false)
    private TopicGroup topicGroup;

//    @Column(name = "topicgroup_id", insertable = false, updatable = false)
//    private UUID topicGroupId;

    @OrderColumn(name="concept_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONCEPT_QUESTION_ITEM", joinColumns = @JoinColumn(name="concept_id", referencedColumnName = "id"))
    private List<ElementRef>  conceptQuestionItems = new ArrayList<>(0);

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


    public List<ElementRef> getConceptQuestionItems() {
        return conceptQuestionItems.stream().filter( Objects::nonNull ).collect(Collectors.toList());
    }

    public void setConceptQuestionItems(List<ElementRef> conceptQuestionItems) {
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


    public List<Concept> getChildren() {
        if (children == null) {
            LOG.error( "ConceptID:"  +getId() +  " -> getChildren is null" );
        }
        return children;
//        return children.stream().filter( Objects::nonNull ).collect(Collectors.toList());
    }

    public void setChildren(List<Concept> children) {
        this.children = children;
    }

    public void addChildren(Concept concept){
        this.setChangeKind(ChangeKind.UPDATED_HIERARCHY_RELATION);

        int index = (concept.getIndex()!=null)? concept.getIndex() : this.getChildren().size();
        this.children.add( index, concept);

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
            TopicGroup topicGroup = findTopicGroup2();
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

    private TopicGroup findTopicGroup2(){
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
        return retvals;
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
                    .map( cqi ->  (QuestionItem)cqi.getElement() )
                    .forEach( item -> {
                    if (item != null) {
                        pdfReport.addheader2( item.getName(), String.format( "Version %s", item.getVersion() ) );
                        pdfReport.addParagraph( item.getQuestion() );
                        if (item.getResponseDomain() != null)
                            item.getResponseDomain().fillDoc( pdfReport, "" );
                    } else {
                        LOG.info( item.toString() );
                    }
                });
            }

            pdfReport.addPadding();

            if (counter.length()>0)
                counter = counter+".";
                int i = 0;
            for (Concept concept : getChildren()) {
                concept.fillDoc(pdfReport, counter + ++i );
            }

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {}


    public Integer getIndex() {
        return index;
    }
}
