package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.elementref.typed.ElementRefTyped;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.history.Revision;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ElementLoader{

    protected final Logger LOG = LoggerFactory.getLogger( this.getClass() );

    protected BaseServiceAudit serviceAudit;

    public ElementLoader(BaseServiceAudit serviceAudit) {
        this.serviceAudit = serviceAudit;
    }

    public IElementRef fill(ElementKind kind, UUID id, Integer rev) {
        return fill( new ElementRef( kind, id, rev ));
    }

    public ElementRefTyped fill(ElementRefTyped element) {
        Revision<Integer,UUID> revision = get(element.getElementId(), element.getElementRevision() );
        element.setElement(revision.getEntity());
        element.setElementRevision( revision.getRevisionNumber() );
        return  element;
    }

    public ElementRef fill(ElementRef element) {
        Revision<Integer,UUID> revision = get(element.getElementId(), element.getElementRevision() );
        element.setElement(revision.getEntity());
        element.setElementRevision( revision.getRevisionNumber() );
        return  element;
    }

    public IElementRef fill(IElementRef element) {
        Revision<Integer,UUID> revision = get(element.getElementId(), element.getElementRevision() );
        element.setElement(revision.getEntity());
        element.setElementRevision( revision.getRevisionNumber() );
        return  element;
    }


    // uses rev Object to facilitate by rev by reference
    private Revision get(UUID id,  Integer rev){
        try {
            if (rev == null) {
                return serviceAudit.findLastChange( id );
            } else
                return serviceAudit.findRevision( id,rev );

        } catch (RevisionDoesNotExistException e) {
            LOG.error( "ElementLoader - RevisionDoesNotExistException ", e );
            if (rev != null)
                return get(id, null);
            else
                throw e;
        } catch (Exception ex) {

            LOG.error( "ElementLoader - fill", ex );
            throw ex;
        }
    }

}
