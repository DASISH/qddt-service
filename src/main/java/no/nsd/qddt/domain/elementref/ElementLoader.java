package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.BaseServiceAudit;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.history.Revision;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ElementLoader<T extends AbstractEntityAudit>{

    protected final Logger LOG = LoggerFactory.getLogger( this.getClass() );

    protected BaseServiceAudit serviceAudit;

    public ElementLoader(BaseServiceAudit serviceAudit) {
        this.serviceAudit = serviceAudit;
    }

    public IElementRef<T>fill(IElementRef<T> element) {
        Revision<Integer, T> revision = get(element.getElementId(), element.getElementRevision() );
        try {
            element.setElement(revision.getEntity());
            element.setElementRevision( revision.getRevisionNumber() );
        } catch (Exception e) {
            LOG.error("ElementLoader setElement, reference has wrong signature");
        }
        return element;
    }

    // uses rev Object to facilitate by rev by reference
    private Revision<Integer,T> get(UUID id, Number rev){
        try {
            
            return (rev == null || rev.intValue() == 0) ? serviceAudit.findLastChange( id ) :  serviceAudit.findRevision( id,rev );

        } catch (RevisionDoesNotExistException e) {
            if (rev == null) throw e;
            LOG.warn( "ElementLoader - RevisionDoesNotExist fallback, fetching latest -> " + id);
            return get(id, null);

        } catch (Exception ex) {

            LOG.error( "ElementLoader - fill", ex );
            throw ex;
        }
    }

}
