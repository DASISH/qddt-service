package no.nsd.qddt.domain.classes.elementref;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstruct.pojo.ConditionConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.StatementItem;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "ELEMENT_REF_NODE")
@AttributeOverride(name="name", column=@Column(name="element_name", length = 1500))
public class ElementRefNode<T extends AbstractEntityAudit> extends AbstractElementRef<T> implements Iterable<ElementRefNode<T>> {

    @Id
    @GeneratedValue(generator ="UUID")
    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
    @Column(name ="id", updatable = false, nullable = false)
    public UUID id;

    @ManyToOne( fetch = FetchType.LAZY, targetEntity = ElementRefNode.class)
    @JsonBackReference(value = "parentRef")
    public ElementRefNode<T> parent;

    // in the @OrderColumn annotation on the referencing entity.
    @Column( name = "parent_idx", insertable = false, updatable = false)
    private int parentIdx;


    @OrderColumn(name="parent_idx")
    @AuditMappedBy(mappedBy = "parent", positionMappedBy = "parentIdx")
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, targetEntity = ElementRefNode.class,
        orphanRemoval = true, cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE})
    public List<ElementRefNode<T>> children;

    @JsonIgnore
    @Transient
    private List<ElementRefNode<T>> elementsIndex;


    public ElementRefNode() {
    }

    public ElementRefNode(T data) {
        this.element = data;
        this.children = new LinkedList<>();
        this.elementsIndex = new LinkedList<>();
        this.elementsIndex.add(this);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    public boolean isRoot() {
        return parent == null;
    }

    // this should make Hibernate fetch children
    public boolean isLeaf() {
        return children.size() == 0;
    }

    public int getLevel() {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }

    public UUID getId() {
        return id;
    }


    public List<ElementRefNode<T>> getChildren() {
        return children;
    }

    public ElementRefNode<T> addChild(T child) {
        ElementRefNode<T> childNode = new ElementRefNode<>( child );
        childNode.parent = this;
        this.children.add(childNode);
        this.registerChildForSearch(childNode);
        return childNode;
    }

    public void checkInNodes() {
        this.children.forEach( c -> {
            c.parent = this;
            c.checkInNodes();
        }  );
    }


    private void registerChildForSearch(ElementRefNode<T> node) {
        elementsIndex.add(node);
        if (parent != null)
            parent.registerChildForSearch(node);
    }



    public ElementRefNode<T> findTreeNode(Comparable<T> cmp) {
        for (ElementRefNode<T> element : this.elementsIndex) {
            T elData = element.element;
            if (cmp.compareTo(elData) == 0)
                return element;
        }

        return null;
    }

    @Override
    public Iterator<ElementRefNode<T>> iterator() {
        return  new ElementRefNodeIter<>( this );
    }

    @Override
    protected AbstractElementRef<T> setValues() {
        if (getElement() == null) return this;
        else if (element instanceof StatementItem)
            setName( getElement().getName() + " ➫ " + ((StatementItem) element).getStatement() );
        else if (element instanceof ConditionConstruct) {
            System.out.println("ignorerer set value");
        }else if (element instanceof QuestionConstruct) {
            //
        } else
            setVersion( getElement().getVersion() );
        if (this.getElementKind() == null)
            setElementKind( ElementKind.getEnum( element.getClass().getSimpleName() ) );
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals( o )) return false;

        ElementRefNode<?> that = (ElementRefNode<?>) o;

        return id.equals( that.id );
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return element != null ? element.toString() : "[data null]";
    }


}

