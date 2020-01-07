package no.nsd.qddt.domain.elementref.typed;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.elementref.AbstractElementRef;
import no.nsd.qddt.domain.elementref.ElementRef;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class ElementRefTyped<T extends AbstractEntityAudit> extends AbstractElementRef {

    public ElementRefTyped() {}

    public ElementRefTyped(AbstractElementRef source ) {
        super(source.getElementKind(),source.getElementId(),source.getElementRevision());
        setVersion(source.getVersion() );
        setName( source.getName());
        setElement( source.getElement() );
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
        return new ElementRefTyped<>( retval );
    }

}
