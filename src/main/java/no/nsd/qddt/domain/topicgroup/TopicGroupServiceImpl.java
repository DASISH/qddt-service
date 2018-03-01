package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.concept.ConceptService;
import no.nsd.qddt.domain.embedded.ElementRef;
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
import org.springframework.data.history.Revision;
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
    private final QuestionItemAuditService questionAuditService;

    @Autowired
    public TopicGroupServiceImpl(TopicGroupRepository topicGroupRepository,
                                 TopicGroupAuditService topicGroupAuditService,
                                 ConceptService conceptService,
                                 QuestionItemAuditService questionItemAuditService) {

        this.topicGroupRepository = topicGroupRepository;
        this.auditService = topicGroupAuditService;
        this.questionAuditService = questionItemAuditService;
        this.conceptService  = conceptService;
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
    public TopicGroup findOne(UUID uuid) {
        return topicGroupRepository.findById(uuid)
            .map(this::postLoadProcessing).orElseThrow(
            () -> new ResourceNotFoundException(uuid, TopicGroup.class)
        );
    }


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public List<TopicGroup> save(List<TopicGroup> instances) {
        return topicGroupRepository.save(instances);
    }

    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public TopicGroup copy(UUID id, Long rev, UUID parentId) {
        //EntityManager entityManager = this.emf.createEntityManager();
        TopicGroup source = auditService.findRevision( id, rev.intValue() ).getEntity();

        TopicGroup target = new TopicGroupFactory().copy(source, rev);
        target.setParentU(parentId);
        return topicGroupRepository.save(target);

/*         Map<UUID,Set<TopicGroupQuestionItem>> tgiRefs  =  copyAlltqi(source);

        try {
            entityManager.detach( source );
            source.makeNewCopy( rev );
            source.setParent( studyService.findOne( parentId ) );
            entityManager.merge( source );
        } finally {
            if(entityManager != null)
                entityManager.close();
        }
        // This is basically wrong, but it all work out nicely in the repository (next load from DB will be correct)
        // remove wrong ref qi's, save instanse, set correct id's on qi's save them to db and attach again.
        TopicGroup finalSource = save( source );
        updateAlltgi( finalSource,tgiRefs );
         return finalSource;
*/
    }


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(UUID uuid) {
        topicGroupRepository.delete(uuid);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(List<TopicGroup> instances) {
        topicGroupRepository.delete(instances);
    }


    private TopicGroup prePersistProcessing(TopicGroup instance) {
        return doArchive( instance ) ;
    }

    @PostLoad
    void test(TopicGroup instance) {
        LOG.debug("Postload " + instance.getName());
    }

    private TopicGroup postLoadProcessing(TopicGroup instance) {
        assert  (instance != null);
        try{
            for (ElementRef<QuestionItem> cqi :instance.getTopicQuestionItems()) {

                Revision<Integer, QuestionItem> rev = questionAuditService.getQuestionItemLastOrRevision(
                    cqi.getId(),
                    cqi.getRevisionNumber().intValue());

                cqi.setElement(rev.getEntity());
                cqi.getElementAs().setConceptRefs(
                    conceptService.findByQuestionItem(cqi.getId()).stream()
                        .map( c -> new ConceptRef(c) )
                        .collect( Collectors.toList())
                );

                if (!cqi.getRevisionNumber().equals(rev.getRevisionNumber().longValue())) {
                    cqi.setRevisionNumber(rev.getRevisionNumber().longValue());
                }
            }
            if (StackTraceFilter.stackContains("getPdf","getXml")) {
                Hibernate.initialize(instance.getConcepts());
                instance.getConcepts().forEach( concept ->
                    concept.getConceptQuestionItems().forEach( cqi ->
                        cqi.setElement(questionAuditService.getQuestionItemLastOrRevision(
                            cqi.getId(),
                            cqi.getRevisionNumber().intValue()).getEntity()) ) );
            }
        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
            }
        return instance;
    }


    @Transactional
    public void delete(TopicGroup instance) {
        topicGroupRepository.delete(instance.getId());
    }

    @Override
    public List<TopicGroup> findByStudyId(UUID id) {
        return topicGroupRepository.findByStudyId(id).stream()
                .map(this::postLoadProcessing).collect(Collectors.toList());
    }

    @Override
    public Page<TopicGroup> findAllPageable(Pageable pageable) {
        return topicGroupRepository.findAll(pageable)
                .map(this::postLoadProcessing);
    }

    @Override
    public Page<TopicGroup> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        return topicGroupRepository.findByNameLikeIgnoreCaseOrAbstractDescriptionLikeIgnoreCase(name,description,pageable)
                .map(this::postLoadProcessing);
    }

}