package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.IEntityAuditXmlRef;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class ElementRef<T extends IEntityAuditXmlRef> extends AbstractElementRef<T> {

    public ElementRef() {
    }

    public ElementRef(ElementKind kind, UUID id, Integer rev) {
        super( kind, id, rev );
    }

    @Override
    public ElementRef<T> clone() {
        ElementRef<T> retval = new ElementRef<T>(getElementKind(), getElementId(),getElementRevision());
        retval.setVersion( getVersion() );
        retval.setName( getName() );
        return retval;
    }



}
