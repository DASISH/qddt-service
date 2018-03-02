package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.PostLoad;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
@Service("topicGroupService")
class TopicGroupServiceImpl implements TopicGroupService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final TopicGroupRepository topicGroupRepository;
    private final TopicGroupAuditService auditService;
    private final ConceptService conceptService;
    private final ElementLoader<QuestionItem> qiLoader;

    @Autowired
    public TopicGroupServiceImpl(TopicGroupRepository topicGroupRepository,
                                 TopicGroupAuditService topicGroupAuditService,
                                 ConceptService conceptService,
                                 QuestionItemAuditService questionItemAuditService) {

        this.topicGroupRepository = topicGroupRepository;
        this.auditService = topicGroupAuditService;
        this.conceptService  = conceptService;
        this.qiLoader = new ElementLoader<>( questionItemAuditService );
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return topicGroupRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return topicGroupRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public TopicGroup findOne(UUID uuid) {
        return topicGroupRepository.findById(uuid)
            .map(this::postLoadProcessing).orElseThrow(
            () -> new ResourceNotFoundException(uuid, TopicGroup.class)
        );
    }


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public TopicGroup save(TopicGroup instance) {
        try {
            instance = postLoadProcessing(
                topicGroupRepository.save(
                    prePersistProcessing(instance)));
        }catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
        }
        return instance;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
    public List<TopicGroup> save(List<TopicGroup> instances) {
        return topicGroupRepository.save(instances);
    }

    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public TopicGroup copy(UUID id, Long rev, UUID parentId) {
        //EntityManager entityManager = this.emf.createEntityManager();
        TopicGroup source = auditService.findRevision( id, rev.intValue() ).getEntity();

        TopicGroup target = new TopicGroupFactory().copy(source, rev);
        target.setParentU(parentId);
        return topicGroupRepository.save(target);
    }


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        topicGroupRepository.delete(uuid);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<TopicGroup> instances) {
        topicGroupRepository.delete(instances);
    }


    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(TopicGroup instance) {
        topicGroupRepository.delete(instance.getId());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<TopicGroup> findByStudyId(UUID id) {
        return topicGroupRepository.findByStudyId(id).stream()
                .map(this::postLoadProcessing).collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<TopicGroup> findAllPageable(Pageable pageable) {
        return topicGroupRepository.findAll(pageable)
                .map(this::postLoadProcessing);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Page<TopicGroup> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        return topicGroupRepository.findByNameLikeIgnoreCaseOrAbstractDescriptionLikeIgnoreCase(name,description,pageable)
                .map(this::postLoadProcessing);
    }


    @PostLoad
    void test(TopicGroup instance) {
        LOG.debug("Postload " + instance.getName());
    }

    private TopicGroup prePersistProcessing(TopicGroup instance) {
        return doArchive( instance ) ;
    }

    private TopicGroup postLoadProcessing(TopicGroup instance) {
        assert  (instance != null);
        try{
            for (ElementRef<QuestionItem> cqi :instance.getTopicQuestionItems()) {

                cqi = qiLoader.fill( cqi );
                cqi.getElementAs().setConceptRefs(
                    conceptService.findByQuestionItem(cqi.getId()).stream()
                        .map( ConceptRef::new )
                        .collect( Collectors.toList())
                );

            }
            if (StackTraceFilter.stackContains("getPdf","getXml")) {
                Hibernate.initialize(instance.getConcepts());
                instance.getConcepts().forEach( concept ->
                    concept.getConceptQuestionItems().forEach( qiLoader::fill ) );
            }
        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map( StackTraceElement::toString )
                .forEach(LOG::info);
        }
        return instance;
    }


}