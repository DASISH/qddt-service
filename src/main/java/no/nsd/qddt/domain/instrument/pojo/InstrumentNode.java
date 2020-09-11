package no.nsd.qddt.domain.instrument.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.controlconstruct.pojo.ConditionConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.StatementItem;
import no.nsd.qddt.domain.elementref.AbstractElementRef;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRefImpl;
import no.nsd.qddt.domain.interfaces.IConditionNode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "INSTRUMENT_NODE")
public class InstrumentNode<T extends ControlConstruct> extends ElementRefImpl<T> implements Iterable<InstrumentNode<T>> {

    @Id
    @GeneratedValue(generator ="UUID")
    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
    @Column(name ="id", updatable = false, nullable = false)
    public UUID id;

    @ManyToOne( fetch = FetchType.LAZY, targetEntity = InstrumentNode.class)
    @JsonBackReference(value = "parentRef")
    public InstrumentNode<T> parent;

    @OrderColumn(name="parent_idx")
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, targetEntity = InstrumentNode.class,
        orphanRemoval = true, cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE})
    public List<InstrumentNode<T>> children;


    @JsonIgnore
    @Transient
    private List<InstrumentNode<T>> elementsIndex;


    @OrderColumn(name="node_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INSTRUMENT_PARAMETER",  joinColumns = @JoinColumn(name="node_id",  referencedColumnName = "id"))
    private List<Parameter> parameters = new ArrayList<>();

    public InstrumentNode() {
    }

    public InstrumentNode(T data) {
        this.element = data;
        this.children = new LinkedList<>();
        this.elementsIndex = new LinkedList<>();
        this.elementsIndex.add(this);

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Parameter parameter) {
        if (!this.parameters.stream().anyMatch( p -> p.getName() == parameter.getName()
            && p.getParameterKind() == parameter.getParameterKind()))
                this.parameters.add( parameter );
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


    public List<InstrumentNode<T>> getChildren() {
        return children;
    }

    public InstrumentNode<T> addChild(T child) {
        InstrumentNode<T> childNode = new InstrumentNode<>( child );
        childNode.parent = this;
        this.children.add(childNode);
        this.registerChildForSearch(childNode);
        return childNode;
    }

    private ConditionNode createConditionNode() {
        try {
            return new ConditionNode( (IConditionNode) getClass().getMethod("getElement").invoke(this ) );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void checkInNodes() {
        if (getElementKind() == ElementKind.CONDITION_CONSTRUCT &&  getElement() != null) {
            System.out.println( getName() + "-01" );

            ConditionNode ref = createConditionNode();
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                .add( "id", getId().toString() )
                .add( "name", ref.getName() )
                .add( "classKind", ref.getClassKind() )
                .add( "conditionKind", ref.getConditionKind().getName() )
                .add( "condition", ref.getCondition() );
            JsonObject jsonObject = objectBuilder.build();

            try (Writer writer = new StringWriter()) {
                Json.createWriter( writer ).write( jsonObject );
                setName( writer.toString() );
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println( getName() + "-02" );
        }
        this.children.forEach( c -> {
            c.parent = this;
            c.checkInNodes();
        }  );
    }


    private void registerChildForSearch(InstrumentNode<T> node) {
        elementsIndex.add(node);
        if (parent != null)
            parent.registerChildForSearch(node);
    }



    public InstrumentNode<T> findTreeNode(Comparable<T> cmp) {
        for (InstrumentNode<T> element : this.elementsIndex) {
            T elData = element.element;
            if (cmp.compareTo(elData) == 0)
                return element;
        }

        return null;
    }

    @Override
    public Iterator<InstrumentNode<T>> iterator() {
        return  new InstrumentNodeIter<>( this );
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

        InstrumentNode<?> that = (InstrumentNode<?>) o;

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

