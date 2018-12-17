package no.nsd.qddt.domain.elementref;

import java.util.UUID;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.elementref.AbstractElementRef;
import no.nsd.qddt.domain.elementref.ElementRef;

/**
 * @author Stig Norland
 */

public class ElementRefTyped<T extends AbstractEntityAudit> extends AbstractElementRef {

    public ElementRefTyped(AbstractElementRef source ) {
        super(source.getElementKind(),source.getElementId(),source.getElementRevision());
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
            ElementRef retval = new ElementRef(getElementKind(), getElementId(),getElementRevision());
            retval.setVersion( getVersion() );
            retval.setName( getName() );
        return new ElementRefTyped<T>( retval );
    }

}
