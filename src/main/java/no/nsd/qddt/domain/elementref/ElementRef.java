package no.nsd.qddt.domain.elementref;

import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class ElementRef<T extends IEntityRef> extends AbstractElementRef<T> {

    public ElementRef() {
    }

    public ElementRef(ElementKind kind, UUID id, Integer rev) {
        super( kind, id, rev );
    }

    @Override
    public ElementRef<T> clone() {
        ElementRef<T> retval = new ElementRef<>(getElementKind(), getElementId(),getElementRevision());
        retval.setVersion( getVersion() );
        retval.setName( getName() );
        return retval;
    }

    @Override
    public String toString() {
        return "{" +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
            "\"version\":" + (getVersion() == null ? "null" : getVersion()) + ", " +
            "\"Kind\":" + (getElementKind() == null ? "null" : getElementKind()) + ", " +
            "\"element\":" + (getElement() == null ? "null" : getElement().getName()) +
            "}";
    }
}
