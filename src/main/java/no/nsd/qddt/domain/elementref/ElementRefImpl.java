package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.interfaces.IWebMenuPreview;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class ElementRefImpl<T extends IWebMenuPreview> extends AbstractElementRef<T> {




    public ElementRefImpl() {
    }

    public ElementRefImpl(ElementKind kind, UUID id, Integer rev) {
        super( kind, id, rev );
    }

    @Override
    public ElementRefImpl<T> clone() {
        ElementRefImpl<T> retval = new ElementRefImpl<>(getElementKind(), getElementId(),getElementRevision());
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
