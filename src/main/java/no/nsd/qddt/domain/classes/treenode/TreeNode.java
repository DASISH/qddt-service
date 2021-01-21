package no.nsd.qddt.domain.classes.treenode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.classes.elementref.AbstractElementRef;
import no.nsd.qddt.domain.classes.interfaces.IDomainObject;
import org.hibernate.annotations.GenericGenerator;
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
public class TreeNode<T extends IDomainObject> extends AbstractElementRef<T> implements Iterable<TreeNode<T>> {

    @Id
    @GeneratedValue(generator ="UUID")
    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
    @Column(name ="id", updatable = false, nullable = false)
    public UUID id;

    @ManyToOne( fetch = FetchType.LAZY, targetEntity = TreeNode.class)
    @JsonBackReference(value = "parentRef")
    public TreeNode<T> parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, targetEntity = TreeNode.class,
        cascade = {CascadeType.MERGE,  CascadeType.REMOVE })
    public List<TreeNode<T>> children;


    @JsonIgnore
    @Transient
    private List<TreeNode<T>> elementsIndex;

    public TreeNode() {
    }

    public TreeNode(T data) {
        this.element = data;
        this.children = new LinkedList<>();
        this.elementsIndex = new LinkedList<>();
        this.elementsIndex.add(this);
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

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<>( child );
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


    private void registerChildForSearch(TreeNode<T> node) {
        elementsIndex.add(node);
        if (parent != null)
            parent.registerChildForSearch(node);
    }



    public TreeNode<T> findTreeNode(Comparable<T> cmp) {
        for (TreeNode<T> element : this.elementsIndex) {
            T elData = element.element;
            if (cmp.compareTo(elData) == 0)
                return element;
        }

        return null;
    }

    @Override
    public String toString() {
        return element != null ? element.toString() : "[data null]";
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        TreeNodeIter<T> iter = new TreeNodeIter<>( this );
        return iter;
    }


}
