package no.nsd.qddt.domain.elementref;

import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class ElementRef extends AbstractElementRef {

    public ElementRef() {
    }

    public ElementRef(ElementKind kind, UUID id, Integer rev) {
        super( kind, id, rev );
    }

    @Override
    public ElementRef clone() {
        ElementRef retval = new ElementRef(getElementKind(), getRefId(),getRevisionNumber());
        retval.setVersion( getVersion() );
        retval.setName( getName() );
        return retval;
    }
}
