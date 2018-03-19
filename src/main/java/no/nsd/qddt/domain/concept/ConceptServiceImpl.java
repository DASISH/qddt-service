package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.domain.AbstractEntityAudit.ChangeKind;
import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Service("conceptService")
class ConceptServiceImpl implements ConceptService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ConceptRepository conceptRepository;
    private final ConceptAuditService auditService;
    private final ElementLoader qiLoader;

    @Autowired
      ConceptServiceImpl(ConceptRepository conceptRepository
            , ConceptAuditService conceptAuditService
            , QuestionItemAuditService questionAuditService){
        this.conceptRepository = conceptRepository;
        this.auditService = conceptAuditService;
        this.qiLoader = new ElementLoader( questionAuditService );
    }

    @Override
    public long count() {
        return conceptRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return conceptRepository.exists(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Concept findOne(UUID uuid) {
        return conceptRepository.findById(uuid).map(this::postLoadProcessing).orElseThrow(
//        return conceptRepository.findById(uuid).orElseThrow(
            () -> new ResourceNotFoundException(uuid, Concept.class));

    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public Concept save(Concept instance) {
        instance = conceptRepository.save( prePersistProcessing( instance ) );
        return postLoadProcessing(instance);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public List<Concept> save(List<Concept> instances) {
        instances.forEach(this::save);
        return instances;
    }

    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
 
    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public Concept copy(UUID id, Integer rev, UUID parentId) {
        EntityManager manager =  emf.createEntityManager();

        Concept source = auditService.findRevision(id,rev).getEntity();
        Concept target = new ConceptFactory().copy(source, rev);
        manager.detach(target);
        target.setParentU(parentId);
        manager.merge(target);
        return conceptRepository.save(target);

    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        try {
            conceptRepository.delete( uuid );
        } catch ( Exception ex ) {
            LOG.error( "Concept delete failed ->", ex );
            throw ex;
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<Concept> instances) {
        conceptRepository.delete(instances);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Concept> findAllPageable(Pageable pageable) {
        Page<Concept> pages = conceptRepository.findAll(defaultSort(pageable,"name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Concept> findByTopicGroupPageable(UUID id, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByTopicGroupIdAndNameIsNotNull(id,
                defaultSort(pageable,"name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Concept> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndBasedOnObjectIsNull(name,description,
                defaultSort(pageable,"name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<Concept> findByQuestionItem(UUID id, Integer rev) {
        return conceptRepository.findByConceptQuestionItemsId(id);
//        return conceptRepository.findByConceptQuestionItemsQuestionId(id);
    }

    private void setChildChangeStatus(Concept concept){
        if (concept.getChangeKind() != ChangeKind.IN_DEVELOPMENT &&
            concept.getChangeKind() != ChangeKind.ARCHIVED ) {

            concept.setChangeKind(ChangeKind.UPDATED_PARENT);
            concept.setChangeComment("Touched to save...");
        }
    }


    private Concept prePersistProcessing(Concept instance) {
        try {
            for (ElementRef element: instance.getConceptQuestionItems()) {
                if (element.getName() == null) {
                    qiLoader.fill( element );
                }
            }


            // children are saved to hold revision info... i guess, these saves shouldn't
            if (instance.isBasedOn() == false)
                instance.getChildren().stream().forEach( this::setChildChangeStatus );
        } catch (NullPointerException npe) {
            LOG.error("ConceptService-> prePersistProcessing " + npe);
            StackTraceFilter.filter(npe.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        } catch(Exception ex) {
            LOG.error("ConceptService-> prePersistProcessing " + instance.getName(), ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
        return instance;
    }

    /*
        post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
        thus we need to populate some elements ourselves.
     */
    protected Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);
        try{
            instance.getConceptQuestionItems().forEach( cqi -> {
                if (cqi.getElement() == null)
                    cqi = qiLoader.fill( cqi );

                ((QuestionItem) cqi.getElementAs())
                    .setConceptRefs(findByQuestionItem(cqi.getId(),null).stream()
                        .map( ConceptRef::new )
                        .collect( Collectors.toList()) );
            } );
        } catch (Exception ex){
            LOG.error("ConceptService.postLoadProcessing",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map( StackTraceElement::toString )
                .forEach(LOG::info);
        }

        instance.getChildren().forEach(this::postLoadProcessing);
        return instance;
    }



}

