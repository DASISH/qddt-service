package no.nsd.qddt.classes.elementref;

import no.nsd.qddt.classes.AbstractEntityAudit;

import java.util.Iterator;

/**
 * @author Stig Norland
 */
public class ElementRefNodeIter<T extends AbstractEntityAudit> implements Iterator<ElementRefNode<T>> {

    enum ProcessStages {
        ProcessParent, ProcessChildCurNode, ProcessChildSubNode
    }

    private final ElementRefNode<T> treeNode;

    public ElementRefNodeIter(ElementRefNode<T> treeNode) {
        this.treeNode = treeNode;
        this.doNext = ElementRefNodeIter.ProcessStages.ProcessParent;
        this.childrenCurNodeIter = treeNode.children.iterator();
    }

    private ElementRefNodeIter.ProcessStages doNext;
    private ElementRefNode<T> next;
    private final Iterator<ElementRefNode<T>> childrenCurNodeIter;
    private Iterator<ElementRefNode<T>> childrenSubNodeIter;

    @Override
    public boolean hasNext() {

        if (this.doNext == ElementRefNodeIter.ProcessStages.ProcessParent) {
            this.next = this.treeNode;
            this.doNext = ElementRefNodeIter.ProcessStages.ProcessChildCurNode;
            return true;
        }

        if (this.doNext == ElementRefNodeIter.ProcessStages.ProcessChildCurNode) {
            if (childrenCurNodeIter.hasNext()) {
                ElementRefNode<T> childDirect = childrenCurNodeIter.next();
                childrenSubNodeIter = childDirect.iterator();
                this.doNext = ElementRefNodeIter.ProcessStages.ProcessChildSubNode;
                return hasNext();
            } else {
                this.doNext = null;
                return false;
            }
        }

        if (this.doNext == ElementRefNodeIter.ProcessStages.ProcessChildSubNode) {
            if (childrenSubNodeIter.hasNext()) {
                this.next = childrenSubNodeIter.next();
                return true;
            } else {
                this.next = null;
                this.doNext = ElementRefNodeIter.ProcessStages.ProcessChildCurNode;
                return hasNext();
            }
        }

        return false;
    }

    @Override
    public ElementRefNode<T> next() {
        return this.next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
