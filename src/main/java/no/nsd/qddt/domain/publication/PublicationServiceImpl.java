package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.concept.json.ConceptJsonEdit;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.elementref.ElementServiceLoader;
import no.nsd.qddt.domain.topicgroup.json.TopicGroupRevisionJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Stig Norland
 */
@Service("selectableService")
public class PublicationServiceImpl implements PublicationService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final PublicationRepository repository;

    private boolean showPrivate= true;

    @Autowired
    ElementServiceLoader serviceLoader;

    @Autowired
    public PublicationServiceImpl(PublicationRepository repository)
    {
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
            return repository.save(instance);
        } catch (Exception ex){
            LOG.error("Publication save ->",ex);
            throw ex;
        }
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public List<Publication> save(List<Publication> instances) {
        return repository.save(instances);
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
    public Page<Publication> findByNameOrPurposeAndStatus(String name, String purpose, String status, Pageable pageable) {
        return repository.findByStatusLikeAndNameIgnoreCaseLikeOrPurposeIgnoreCaseLike(status,name,purpose,
                defaultSort(pageable,"name","modified"));
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
        return instance;
    }

    private Publication postLoadProcessing(Publication instance) {
        if (instance.getStatus().toLowerCase().contains("public")|| instance.getStatus().toLowerCase().contains("extern"))
            showPrivate = false;

        instance.getPublicationElements().forEach(e-> postLoadProcessing(e));

        return instance;
    }

    ElementRef postLoadProcessing(ElementRef instance) {
        instance = new ElementLoader(serviceLoader.getService( instance.getElementKind())).fill( instance );
        switch (instance.getElementKind()) {
            case TOPIC_GROUP:
                ((TopicGroupRevisionJson)instance.getElement()).getTopicQuestionItems()
                    .forEach(e-> postLoadProcessing(e));
                ((TopicGroupRevisionJson)instance.getElement()).getConcepts()
                    .forEach( c->c.getConceptQuestionItems()
                        .forEach( e-> postLoadProcessing(e) ) );
                break;
            case CONCEPT:
                ((ConceptJsonEdit)instance.getElement()).getConceptQuestionItems()
                    .forEach(e-> postLoadProcessing(e));
                break;
            case CONTROL_CONSTRUCT:
            case QUESTION_CONSTRUCT:
                break;
            default:
                // do nothing
        }
        return instance;
    }


}
