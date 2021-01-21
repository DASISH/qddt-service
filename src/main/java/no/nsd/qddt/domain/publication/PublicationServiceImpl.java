package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.classes.elementref.ElementLoader;
import no.nsd.qddt.domain.classes.elementref.ElementRefEmbedded;
import no.nsd.qddt.domain.classes.elementref.ElementServiceLoader;
import no.nsd.qddt.domain.classes.exception.StackTraceFilter;
import no.nsd.qddt.domain.classes.interfaces.IElementRef;
import no.nsd.qddt.domain.publication.audit.PublicationAuditService;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus.Published;
import no.nsd.qddt.domain.user.User;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.StringTool.IsNullOrEmpty;
import static no.nsd.qddt.utils.StringTool.likeify;
/**
 * @author Stig Norland
 */
@Service("publicationService")
public class PublicationServiceImpl implements PublicationService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final PublicationRepository repository;
    private final PublicationAuditService auditService;

    private boolean showPrivate= true;

    @Autowired
    ElementServiceLoader serviceLoader;


    @Autowired
    public PublicationServiceImpl(PublicationRepository repository
                                  ,PublicationAuditService publicationAuditService)
    {
        this.auditService = publicationAuditService;
        this.repository = repository;
    }

    @Override
    public long count() {
        return repository.count();
    }


    @Override
    public boolean exists(UUID uuid) {
        return repository.existsById(uuid);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW','ROLE_GUEST')")
    public Publication findOne(UUID uuid) {
        return postLoadProcessing(repository.findById(uuid).get());
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR') and hasPermission(#instance,'AGENCY')")
    public Publication save(Publication instance) {
        try {
            return  postLoadProcessing(
                repository.save(
                    prePersistProcessing( instance)));

        } catch (Exception ex){
            LOG.error("Publication save ->",ex);
            throw ex;
        }
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW','ROLE_GUEST')")
    public Page<Publication> findByNameOrPurposeAndStatus(String name, String purpose, String publicationStatus, String publishedKind, Pageable pageable) {

        if (name.isEmpty()  &&  purpose.isEmpty() && publicationStatus.isEmpty()) {
            name = "%";
        }
        if (IsNullOrEmpty(publicationStatus))
            publicationStatus = "#";

        String statuses = Arrays.stream(publicationStatus.split( " ")).map( val -> val.trim() ).collect( Collectors.joining("|") );
        statuses= "%(" + statuses + ")%";
        Published published = Published.valueOf( publishedKind );

        LOG.info("findByNameOrPurposeAndStatus2 " + published + " name: " + name + " purpose: " + purpose + " statuses: " +  statuses);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if(user.getAuthorities().stream().anyMatch( p -> p.getAuthority().equals("ROLE_GUEST"))) {
            published = Published.EXTERNAL_PUBLICATION;
        }
        if (user.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ROLE_VIEW"))) {
            if (published.equals( Published.NOT_PUBLISHED ))
            published = Published.INTERNAL_PUBLICATION;
        }

        return repository.findByQuery(likeify(name),likeify(purpose),statuses, published.name(),user.getAgency().getId() ,defaultOrModifiedSort(pageable,"name"));

    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW','ROLE_GUEST')")
    public ElementRefEmbedded<?> getDetail(IElementRef publicationElement) {
        return (ElementRefEmbedded<?>)
            new ElementLoader<>( serviceLoader.getService( publicationElement.getElementKind() ) )
                .fill( publicationElement );
    }


    protected Publication prePersistProcessing(Publication instance) {
        Integer rev = null;
        if(instance.isBasedOn())
            rev = auditService.findLastChange(instance.getId()).getRevisionNumber().get();

        if (instance.isBasedOn() || instance.isNewCopy()) {
            instance = new PublicationFactory().copy( instance, rev );
            LOG.info( " PublicationFactory().copy " );
        }
        return instance;

    }

    private Publication postLoadProcessing(Publication instance) {
        if (instance.getStatus().getPublished().ordinal() > Published.NOT_PUBLISHED.ordinal())
            showPrivate = false;
        Hibernate.initialize(instance.getPublicationElements());
        if (StackTraceFilter.stackContains( "getPdf", "getXml" )) {
            instance.getPublicationElements().forEach( this::loadDetail );
        }
        instance.setChangeComment( null );
        return instance;
    }


    public PublicationElement loadDetail(IElementRef element) {
        return (PublicationElement) new ElementLoader<>( serviceLoader.getService( element.getElementKind() ) ).fill( element );
    }


}
