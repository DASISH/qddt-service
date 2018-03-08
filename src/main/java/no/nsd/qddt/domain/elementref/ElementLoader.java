package no.nsd.qddt.domain.elementref;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.elementref.typed.ElementRefTyped;
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
public class ElementLoader{

    protected final Logger LOG = LoggerFactory.getLogger( this.getClass() );

//    @Autowired
//    private ElementServiceLoader serviceLoader;

    protected BaseServiceAudit serviceAudit;

    public ElementLoader() {
    }

    public ElementLoader(BaseServiceAudit serviceAudit) {
        this.serviceAudit = serviceAudit;
    }

    public ElementRef fill(ElementKind kind, UUID id, Integer rev) {
        return fill( new ElementRef( kind, id, rev ));
    }

    public ElementRefTyped fill(ElementRefTyped element) {
        Integer rev = element.getRevisionNumber().intValue();
        element.setElement(get(element.getId(), rev ));
        element.setRevisionNumber( rev.longValue() );
        return  element;
    }

    public ElementRef fill(ElementRef element) {
        Integer rev = element.getRevisionNumber().intValue();
        element.setElement(get(element.getId(), rev ));
        element.setRevisionNumber( rev.longValue() );
        return  element;
    }


    private Object get(UUID id, Integer rev){
        try {

            Revision revision = serviceAudit.findRevision( id, rev );
            return revision.getEntity();

        } catch (RevisionDoesNotExistException e) {
            LOG.info( "ElementLoader - RevisionDoesNotExistException ", e );
            Revision revision = serviceAudit.findLastChange( id );
            rev = revision.getRevisionNumber().intValue();
            return revision.getEntity();

        } catch (JpaSystemException se) {

            LOG.error( "ElementLoader - JpaSystemException", se );
            StackTraceFilter.filter( se.getStackTrace() ).stream()
                .map( a -> a.toString() )
                .forEach( LOG::debug );

        } catch (Exception ex) {

            LOG.error( "ElementLoader - fill", ex );
            StackTraceFilter.filter( ex.getStackTrace() ).stream()
                .map( a -> a.toString() )
                .forEach( LOG::debug );

        }
        return null;
    }



}