package no.nsd.qddt.domain.instrument.pojo;


import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;

import java.util.Iterator;

/**
 * @author Stig Norland
 * https://github.com/gt4dev/yet-another-tree-structure/blob/master/java/src/com/tree/TreeNodeIter.java
 */
public class InstrumentNodeIter<T extends ControlConstruct> implements Iterator<InstrumentNode<T>> {

    enum ProcessStages {
        ProcessParent, ProcessChildCurNode, ProcessChildSubNode
    }

    private final InstrumentNode<T> treeNode;

    public InstrumentNodeIter(InstrumentNode<T> treeNode) {
        this.treeNode = treeNode;
        this.doNext = ProcessStages.ProcessParent;
        this.childrenCurNodeIter = treeNode.children.iterator();
    }

    private ProcessStages doNext;
    private InstrumentNode<T> next;
    private final Iterator<InstrumentNode<T>> childrenCurNodeIter;
    private Iterator<InstrumentNode<T>> childrenSubNodeIter;

    @Override
    public boolean hasNext() {

        if (this.doNext == ProcessStages.ProcessParent) {
            this.next = this.treeNode;
            this.doNext = ProcessStages.ProcessChildCurNode;
            return true;
        }

        if (this.doNext == ProcessStages.ProcessChildCurNode) {
            if (childrenCurNodeIter.hasNext()) {
                InstrumentNode<T> childDirect = childrenCurNodeIter.next();
                childrenSubNodeIter = childDirect.iterator();
                this.doNext = ProcessStages.ProcessChildSubNode;
                return hasNext();
            }

            else {
                this.doNext = null;
                return false;
            }
        }

        if (this.doNext == ProcessStages.ProcessChildSubNode) {
            if (childrenSubNodeIter.hasNext()) {
                this.next = childrenSubNodeIter.next();
                return true;
            }
            else {
                this.next = null;
                this.doNext = ProcessStages.ProcessChildCurNode;
                return hasNext();
            }
        }

        return false;
    }

    @Override
    public InstrumentNode<T> next() {
        return this.next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
