package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItem;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostLoad;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
@Service("topicGroupService")
class TopicGroupServiceImpl implements TopicGroupService {

    private final TopicGroupRepository topicGroupRepository;
    private final TopicGroupAuditService auditService;
    private final QuestionItemAuditService questionAuditService;

    @Autowired
    public TopicGroupServiceImpl(TopicGroupRepository topicGroupRepository,
                                 TopicGroupAuditService topicGroupAuditService,
                                 QuestionItemAuditService questionItemAuditService) {
        this.topicGroupRepository = topicGroupRepository;
        this.auditService = topicGroupAuditService;
        this.questionAuditService = questionItemAuditService;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return topicGroupRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return topicGroupRepository.existsById(uuid);
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

//    @Override
//    @Transactional
//    public List<TopicGroup> save(List<TopicGroup> instances) {
//        return topicGroupRepository.saveAll(instances);
//    }


    @Override
    @Transactional
    public void delete(UUID uuid) {
        topicGroupRepository.deleteById(uuid);
    }

    @Override
    @Transactional
    public void delete(List<TopicGroup> instances) {
        topicGroupRepository.deleteAll(instances);
    }


    private TopicGroup prePersistProcessing(TopicGroup instance) {
        if (instance.getChangeKind() == AbstractEntityAudit.ChangeKind.ARCHIVED) {
            String changecomment =  instance.getChangeComment();
            instance = findOne(instance.getId());
            instance.setArchived(true);
            instance.setChangeComment(changecomment);
        }
        if (instance.isBasedOn()){
            Revision<Long, TopicGroup> lastChange
                    = auditService.findLastChange(instance.getId());
            instance.makeNewCopy(lastChange.getRevisionNumber().get());
        }

        if( instance.isNewCopy()){
            instance.makeNewCopy(null);
        }
        return instance;
    }

    @PostLoad void test(TopicGroup instance) {
        System.out.println("Postload " + instance.getName());
    }

    private TopicGroup postLoadProcessing(TopicGroup instance) {
        assert  (instance != null);
        try{
            for (TopicGroupQuestionItem cqi :instance.getTopicQuestionItems()) {
                Revision<Long, QuestionItem> rev = questionAuditService.getQuestionItemLastOrRevision(
                        cqi.getId().getQuestionItemId(),
                        cqi.getQuestionItemRevision());
                cqi.setQuestionItem(rev.getEntity());
                if (!cqi.getQuestionItemRevision().equals(rev.getRevisionNumber().get())) {
                    cqi.setQuestionItemRevision(rev.getRevisionNumber().get());
                }
            }
            if (StackTraceFilter.stackContains("getPdf","getXml")) {
                instance.getConcepts().size();
            }
        } catch (Exception ex){
            System.out.println("postLoadProcessing... " + instance.getName());
            StackTraceFilter.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
        }
        return instance;
    }


    @Transactional
    public void delete(TopicGroup instance) {
        topicGroupRepository.deleteById(instance.getId());
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