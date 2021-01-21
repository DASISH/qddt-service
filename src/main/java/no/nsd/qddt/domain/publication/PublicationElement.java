package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.classes.elementref.AbstractElementRef;
import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.classes.interfaces.IDomainObject;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class PublicationElement extends AbstractElementRef<IDomainObject> {

    @Override
    public IDomainObject getElement() {
        return super.getElement();
    }

    public PublicationElement() {
    }

    public PublicationElement(ElementKind elementKind, UUID elementId, Integer elementRevision) {
        super(elementKind,elementId,elementRevision);
    }

    @Override
    public PublicationElement clone() {
        PublicationElement retval = new PublicationElement(getElementKind(), getElementId(),getElementRevision());
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
