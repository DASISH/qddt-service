package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.AbstractEntityAudit.ChangeKind;
import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ParentRef;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.exception.DescendantsArchivedException;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.FilterTool.defaultSort;
import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;
import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Service("conceptService")
class ConceptServiceImpl implements ConceptService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ConceptRepository conceptRepository;
    private final ConceptAuditService auditService;
    private final TopicGroupService tgService;
    private final ElementLoader<QuestionItem> qiLoader;

    @Autowired
      ConceptServiceImpl(ConceptRepository conceptRepository
            , ConceptAuditService conceptAuditService
            , QuestionItemAuditService questionAuditService
            , TopicGroupService tgService){
        this.conceptRepository = conceptRepository;
        this.auditService = conceptAuditService;
        this.tgService = tgService;
        this.qiLoader = new ElementLoader<>( questionAuditService );
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
            () -> new ResourceNotFoundException(uuid, Concept.class));

    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT') and hasPermission(#instance,'AGENCY')")
    public Concept save(Concept instance) {
        instance = conceptRepository.saveAndFlush(prePersistProcessing( instance ) );
        return postLoadProcessing(instance);
    }

    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public List<Concept> save(List<Concept> instances) {
        instances.forEach(this::save);
        return instances;
    }

 
    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public Concept copy(UUID id, Integer rev, UUID parentId) {
        Concept source = auditService.findRevision(id,rev).getEntity();
        Concept target = new ConceptFactory().copy(source, rev);
        TopicGroup tg = tgService.findOne( parentId );
        target = tg.addConcept( target );
        return conceptRepository.save(target);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        LOG.info( "deleting " + uuid );
        if (conceptRepository.hasArchive( uuid.toString() ) > 0)
            throw new DescendantsArchivedException( uuid.toString() );
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
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Concept> findAllPageable(Pageable pageable) {
        Page<Concept> pages = conceptRepository.findAll(defaultSort(pageable,"name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Concept> findByTopicGroupPageable(UUID id, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByTopicGroupIdAndNameIsNotNull(id,
                defaultSort(pageable,"name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<Concept> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        if (name.isEmpty()  &&  description.isEmpty()) {
            name = "%";
        }
        return conceptRepository.findByQuery(
            likeify(name),
            likeify(description),
            defaultOrModifiedSort(pageable,"name ASC")).map(this::postLoadProcessing);
    }

    @Override
    public List<Concept> findByTopicGroup(UUID id) {
        return tgService.findOne(id).getConcepts().stream()
            .map( this::postLoadProcessing)
            .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<Concept> findByQuestionItem(UUID id, Integer rev) {
        return conceptRepository.findByConceptQuestionItemsElementId(id);
    }

    private void setChildChangeStatus(Concept concept){
// children don't need to be touched for parent to see them... as they are...
// however, they are saved as part of presisting, so we need to see correct status

        if (concept.getChangeKind() != ChangeKind.IN_DEVELOPMENT &&
            concept.getChangeKind() != ChangeKind.ARCHIVED ) {

            concept.setChangeKind(ChangeKind.UPDATED_PARENT);
            concept.setChangeComment("Touched to keep flag in sync...");
        }
    }


    private Concept prePersistProcessing(Concept instance) {
        try {
            instance.getChildren().stream().forEach( this::setChildChangeStatus );
            if (instance.hasTopicGroup()) {
                if(instance.getParentTopicGroup() == null) {
                    LOG.info( "Topicgroup not set !!!" );
//                    tgService.findOne( instance. )
                    instance.getParentTopicGroup().addConcept( instance );
                } else if (instance.getId() == null) {
                    instance.getParentTopicGroup().addConcept( instance );
                } else {
                    if (!instance.getParentTopicGroup().getConcepts().stream().anyMatch( p -> p.getId() == instance.getId() ))
                        instance.getParentTopicGroup().addConcept( instance );
                }
            }
        } catch (NullPointerException npe) {
            LOG.error("ConceptService-> prePersistProcessing " + npe);
        } catch(Exception ex) {
            LOG.error("ConceptService-> prePersistProcessing " + instance.getName(), ex);
        }
        return instance;
    }

    /*
        post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
        thus we need to populate some elements ourselves.
     */
    protected Concept postLoadProcessing(Concept instance) {
        if (instance == null) return null;
        try {
            if (StackTraceFilter.stackContains("getPdf","getXml")) {
                LOG.debug("PDF-XML -> fetching  QuestionItems ");
                instance.getConceptQuestionItems().forEach( cqi -> qiLoader.fill( cqi ));
            } else {
                instance.getConceptQuestionItems().forEach( cqi -> {
                    if (IsNullOrTrimEmpty( cqi.getName() )) {
                        qiLoader.fill( cqi );
                        cqi.getElement().setParentRefs(
                            findByQuestionItem(cqi.getElementId(),null).stream()
                                .map( ParentRef<Concept>::new )
                                .collect( Collectors.toList()));
                    }
                });
            }
        } catch (Exception ex){
            LOG.error("ConceptService.postLoadProcessing",ex);
        }

        instance.getChildren().forEach(this::postLoadProcessing);
        return instance;
    }


    public boolean hasArchivedContent(UUID id) {
        return false;
    }
}

