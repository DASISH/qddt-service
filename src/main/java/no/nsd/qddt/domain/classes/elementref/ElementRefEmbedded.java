package no.nsd.qddt.domain.classes.elementref;

import no.nsd.qddt.domain.classes.interfaces.IWebMenuPreview;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@Embeddable
public class ElementRefEmbedded<T extends IWebMenuPreview> extends AbstractElementRef<T> implements Serializable {
    private static final long serialVersionUID = 3206987451754010936L;

    public ElementRefEmbedded() {
    }

    public ElementRefEmbedded(ElementKind kind, UUID id, Integer rev) {
        super( kind, id, rev );
    }

    @Override
    public ElementRefEmbedded<T> clone() {
        ElementRefEmbedded<T> retval = new ElementRefEmbedded<>(getElementKind(), getElementId(),getElementRevision());
        retval.setVersion( getVersion() );
        retval.setName( getName() );
        return retval;
    }
    

}
