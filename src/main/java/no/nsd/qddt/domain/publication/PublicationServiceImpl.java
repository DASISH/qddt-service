package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.elementref.ElementServiceLoader;
import no.nsd.qddt.domain.publication.audit.PublicationAuditService;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus.Published;
import no.nsd.qddt.utils.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.FilterTool.defaultSort;
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
        return repository.exists(uuid);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW','ROLE_GUEST')")
    public Publication findOne(UUID uuid) {
        return postLoadProcessing(repository.findOne(uuid));
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
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
        repository.delete(uuid);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<Publication> instances) {
        repository.delete(instances);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW','ROLE_GUEST')")
    public Page<Publication> findAllPageable(Pageable pageable) {
        return repository.findAll(defaultSort(pageable,"name","modified"));
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

        if (SecurityContext.getUserDetails().getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ROLE_GUEST"))) {
            published = Published.EXTERNAL_PUBLICATION;
        }
        if (SecurityContext.getUserDetails().getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ROLE_VIEW"))) {
            if (published.equals( Published.NOT_PUBLISHED ))
            published = Published.INTERNAL_PUBLICATION;
        }

        return repository.findByQuery(likeify(name),likeify(purpose),statuses, published.name() ,defaultOrModifiedSort(pageable,"name"));

    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW','ROLE_GUEST')")
    public ElementRef getDetail(ElementRef publicationElement) {
        return new ElementLoader(
                serviceLoader.getService(
                    publicationElement.getElementKind() 
                )).fill( publicationElement );
    }


    protected Publication prePersistProcessing(Publication instance) {
        Integer rev = null;
        if(instance.isBasedOn())
            rev= auditService.findLastChange(instance.getId()).getRevisionNumber();

        if (instance.isBasedOn() || instance.isNewCopy()) {
            instance = new PublicationFactory().copy( instance, rev );
            LOG.info( " PublicationFactory().copy " );
        }
        return instance;

    }

    private Publication postLoadProcessing(Publication instance) {
        if (instance.getStatus().getPublished().ordinal() > Published.NOT_PUBLISHED.ordinal())
            showPrivate = false;

        instance.getPublicationElements().forEach(e-> postLoadProcessing(e));

        return instance;
    }

    ElementRef postLoadProcessing(ElementRef instance) {
//        try {
//            instance = new ElementLoader( serviceLoader.getService( instance.getElementKind() ) ).fill( instance );
//            LOG.info( instance.getName() + " " + instance.getElementKind());
//        } catch (InvalidDataAccessApiUsageException ida) {
//            return instance;
//        } catch (Exception e) {
//            LOG.error("postLoadProcessing " ,e );
//            return instance;
//        }

//        switch (instance.getElementKind()) {
//            case TOPIC_GROUP:
//                ((TopicGroup)instance.getElement()).getTopicQuestionItems()
//                    .forEach(e-> postLoadProcessing(e));
//                ((TopicGroup)instance.getElement()).getConcepts()
//                    .forEach( c->c.getConceptQuestionItems()
//                        .forEach( e-> postLoadProcessing(e) ) );
//                break;
//            case CONCEPT:
//                ((Concept)instance.getElement()).getConceptQuestionItems()
//                    .forEach(e-> postLoadProcessing(e));
//                break;
//            case CONTROL_CONSTRUCT:
//            case QUESTION_CONSTRUCT:
//                break;
//            default:
//                // do nothing
//        }
        return instance;
    }

}
