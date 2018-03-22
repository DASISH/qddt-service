package no.nsd.qddt.domain.elementref.typed;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.elementref.AbstractElementRef;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRef;

import java.util.UUID;

/**
 * @author Stig Norland
 */

public class ElementRefTyped<T extends AbstractEntityAudit> extends AbstractElementRef {

    public ElementRefTyped(ElementRef source ) {
        super(source.getElementKind(),source.getRefId(),source.getRevisionNumber());
        setVersion(source.getVersion() );
        setName( source.getName());
        setElement( source.getElement() );
    }

    public ElementRefTyped(ElementKind kind, UUID id, Integer rev) {
        super( kind, id, rev );
    }

    @Override
    public T getElement() {
        return (T)super.element;
    }

    @Override
    public ElementRefTyped<T> clone() {
            ElementRef retval = new ElementRef(getElementKind(), getRefId(),getRevisionNumber());
            retval.setVersion( getVersion() );
            retval.setName( getName() );
        return new ElementRefTyped<>( retval);
    }

}
