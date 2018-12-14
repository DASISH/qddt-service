package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import no.nsd.qddt.exception.DescendantsArchivedException;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;

/**
 * @author Stig Norland
 */
@Service("topicGroupService")
class TopicGroupServiceImpl implements TopicGroupService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ElementLoader qiLoader;
    private final TopicGroupRepository topicGroupRepository;
    private final TopicGroupAuditService auditService;
    private final StudyService studyService;

    @Autowired
    public TopicGroupServiceImpl(TopicGroupRepository topicGroupRepository,
                                 TopicGroupAuditService topicGroupAuditService,
                                 QuestionItemAuditService questionItemAuditService,
                                 StudyService studyService) {

        this.qiLoader = new ElementLoader( questionItemAuditService );
        this.topicGroupRepository = topicGroupRepository;
        this.auditService = topicGroupAuditService;
        this.studyService = studyService;
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
        return postLoadProcessing(topicGroupRepository.findById(uuid).orElseThrow(
            () -> new ResourceNotFoundException(uuid, TopicGroup.class))
        );
    }


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')  and hasPermission(#instance,'AGENCY')")
    public TopicGroup save(TopicGroup instance) {
        try {
            return postLoadProcessing(
                topicGroupRepository.save(
                    prePersistProcessing(instance)));
        } catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            throw ex;
        }
    }

//    @Transactional
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT')")
//    public List<TopicGroup> save(List<TopicGroup> instances) {
//        return topicGroupRepository.save(instances);
//    }

//    private EntityManagerFactory emf;

//    @PersistenceUnit
//    public void setEntityManagerFactory(EntityManagerFactory emf) {
//        this.emf = emf;
//    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public TopicGroup copy(UUID id, Integer rev, UUID parentId) {
        TopicGroup source = auditService.findRevision( id, rev ).getEntity();
//        TopicGroup copy = new TopicGroupFactory().copy(source, rev);
//        studyService.findOne( parentId ).addTopicGroup( copy );
//        entityManager.detach( target );
//        target.setParentU(parentId);
//        entityManager.merge( target );
        return topicGroupRepository.save(
            studyService.findOne( parentId )
                .addTopicGroup( new TopicGroupFactory().copy(source, rev) ));
    }


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        if (topicGroupRepository.hasArchive( uuid.toString() ) > 0)
            throw new DescendantsArchivedException( uuid.toString() );
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
    // Only users that can see survey and study can here, (sometimes guest should see this too.)
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<TopicGroup> findByStudyId(UUID id) {
        return topicGroupRepository.findByStudyIdOrderByStudyIndex(id).stream().filter( f -> f != null )
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
        if (name.isEmpty()  &&  description.isEmpty()) {
            name = "%";
        }
        return topicGroupRepository.findByQuery(name,description,pageable)
                .map(this::postLoadProcessing);
    }


    private TopicGroup prePersistProcessing(TopicGroup instance) {
        for (ElementRef cqi: instance.getTopicQuestionItems()) {
            if (IsNullOrTrimEmpty( cqi.getName())) {
                qiLoader.fill( cqi ).setValues();
            }
        }
        return doArchive( instance ) ;
    }

    private TopicGroup postLoadProcessing(TopicGroup instance) {
        assert  (instance != null);
        try{

            if (StackTraceFilter.stackContains("getPdf","getXml")) {
                LOG.debug("PDF -> fetching  concepts and QuestionItems ");
                instance.getTopicQuestionItems().forEach( cqi -> qiLoader.fill( cqi ));
                Hibernate.initialize(instance.getConcepts());
                instance.getConcepts().forEach( this::loadConceptQuestion );

                // when we print hierarchy we don't need qi concept reference....
            }
        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
            throw ex;
        }
        LOG.debug("TopicGroupIdx " + (instance.getIndex()==null?"NULL":instance.getIndex()) );
        return instance;
    }


    private void loadConceptQuestion(Concept parent) {
        parent.getChildren().forEach( this::loadConceptQuestion );
        parent.getConceptQuestionItems().forEach( qiLoader::fill );
    }

}