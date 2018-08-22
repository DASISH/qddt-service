package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.BaseServiceAudit;
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

//    public IElementRef fill(ElementKind kind, UUID id, Integer rev) {
//        return fill( new ElementRef( kind, id, rev ));
//    }
//
//    public ElementRefTyped<no.nsd.qddt.domain.AbstractEntityAudit> fill(ElementRefTyped<no.nsd.qddt.domain.AbstractEntityAudit> element) {
//        Revision<Integer,UUID> revision = get(element.getElementId(), element.getElementRevision() );
//        element.setElement(revision.getEntity());
//        element.setElementRevision( revision.getRevisionNumber() );
//        return  element;
//    }

    public ElementRef fill(ElementRef element) {
        Revision<Integer,UUID> revision = get(element.getElementId(), element.getElementRevision() );
        element.setElement(revision.getEntity());
        element.setElementRevision( revision.getRevisionNumber() );
        return  element;
    }

    public IElementRef fill(IElementRef element) {
        LOG.debug(element.toString());
        Revision<Integer,UUID> revision = get(element.getElementId(), element.getElementRevision() );
        element.setElement(revision.getEntity());
        element.setElementRevision( revision.getRevisionNumber() );
        return  element;
    }


    // uses rev Object to facilitate by rev by reference
    private Revision get(UUID id,  Integer rev){
        try {
            
            return (rev == null) ? serviceAudit.findLastChange( id ) :  serviceAudit.findRevision( id,rev );

        } catch (RevisionDoesNotExistException e) {

            LOG.error( "ElementLoader - RevisionDoesNotExistException ", e );
            if (rev == null) throw e;
            return get(id, null);

        } catch (Exception ex) {

            LOG.error( "ElementLoader - fill", ex );
            throw ex;
        }
    }

}
