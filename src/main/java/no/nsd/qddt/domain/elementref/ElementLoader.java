package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.history.Revision;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ElementLoader<T extends AbstractEntityAudit> {

    protected final Logger LOG = LoggerFactory.getLogger( this.getClass() );
    private BaseServiceAudit<T, UUID, Integer> serviceAudit;

    public ElementLoader(BaseServiceAudit<T, UUID,Integer> serviceAudit) {
        this.serviceAudit = serviceAudit;
    }

    public ElementRef<T> fill(ElementRef<T> element) {
        try {
            Revision<Integer,T> revision = serviceAudit.findRevision(element.getId(),element.getRevisionNumber().intValue());
            element.setElement( revision.getEntity() );
            LOG.info(element.toString());    

        } catch (RevisionDoesNotExistException e) {
            LOG.error("PublicationElement - RevisionDoesNotExistException ", e);
            Revision<Integer,T> revision = serviceAudit.findLastChange(element.getId());
            element.setElement(revision.getEntity());
            element.setRevisionNumber(revision.getRevisionNumber().longValue());

        } catch (JpaSystemException se) {

            LOG.error("PublicationElement - JpaSystemException",se);
            StackTraceFilter.filter(se.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);

        } catch (Exception ex) {

            LOG.error( "PublicationElement - fill", ex );
            StackTraceFilter.filter( ex.getStackTrace() ).stream()
                .map( a -> a.toString() )
                .forEach( LOG::info );

        }
        return element;
    }

    public ElementRef<T> fill(ElementKind kind, UUID id, Integer rev) {
        return fill( new ElementRef<>( kind, id, rev ));
    }

}