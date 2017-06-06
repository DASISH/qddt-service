package no.nsd.qddt.domain.topicgroup;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItemJson;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.questionItem.json.QuestionItemJsonView;
import no.nsd.qddt.domain.topicgroup.audit.TopicGroupAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("topicGroupService")
class TopicGroupServiceImpl implements TopicGroupService {

    private TopicGroupRepository topicGroupRepository;
    private TopicGroupAuditService auditService;
    private QuestionItemAuditService questionAuditService;

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
        return topicGroupRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public TopicGroup findOne(UUID uuid) {
        return topicGroupRepository.findById(uuid).orElseThrow(
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
            ex.printStackTrace();
        }
        return instance;
    }

    @Override
    @Transactional
    public List<TopicGroup> save(List<TopicGroup> instances) {
        return topicGroupRepository.save(instances);
    }


    @Override
    @Transactional
    public void delete(UUID uuid) {
        topicGroupRepository.delete(uuid);
    }

    @Override
    @Transactional
    public void delete(List<TopicGroup> instances) {
        topicGroupRepository.delete(instances);
    }


    protected TopicGroup prePersistProcessing(TopicGroup instance) {
        System.out.println("prePersistProcessing");
        if(instance.getConcepts().isEmpty() & instance.getVersion().isNew()){
             instance.getConcepts().add(new Concept());
        }

        if (instance.isBasedOn()){
            Revision<Integer, TopicGroup> lastChange
                    = auditService.findLastChange(instance.getId());
            instance.makeNewCopy(lastChange.getRevisionNumber());
        }

        if( instance.isNewCopy()){
            instance.makeNewCopy(null);
        }
        return instance;
    }


    protected TopicGroup postLoadProcessing(TopicGroup instance) {

        for (ConceptQuestionItem cqi :instance.getTopicQuestions().getQuestionItems()) {
            Revision<Integer, QuestionItem> rev = questionAuditService.getQuestionItemLastOrRevision(
                    cqi.getId().getQuestionItemId(),
                    cqi.getQuestionItemRevision());
            System.out.println("postLoadProcessing TopicGroup " + rev.toString() +  cqi.getId());
            cqi.setQuestionItem(rev.getEntity());
        }

        return instance;
    }


    @Transactional
    public void delete(TopicGroup instance) {
        topicGroupRepository.delete(instance.getId());
    }

    @Override
    public List<TopicGroup> findByStudyId(UUID id) {
        return topicGroupRepository.findByStudyId(id);
    }

    @Override
    public Page<TopicGroup> findAllPageable(Pageable pageable) {
        return topicGroupRepository.findAll(pageable);
    }

    @Override
    public Page<TopicGroup> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        return topicGroupRepository.findByNameLikeIgnoreCaseOrAbstractDescriptionLikeIgnoreCase(name,description,pageable);
    }
}