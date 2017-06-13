package no.nsd.qddt.domain.topicgroupquestionitem;

import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItemId;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.exception.RequestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
@Service("conceptQuestionItemService")
public class TopicGroupQuestionItemServiceImpl implements TopicGroupQuestionItemService {

    private TopicGroupQuestionItemRepository repository;
    private QuestionItemAuditService auditService;

    @Autowired
    public TopicGroupQuestionItemServiceImpl(TopicGroupQuestionItemRepository repository, QuestionItemAuditService questionItemAuditService) {
        this.repository = repository;
        this.auditService = questionItemAuditService;
    }


    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean exists(ParentQuestionItemId parentQuestionItemId) {
        return repository.exists(parentQuestionItemId);
    }

    @Override
    public TopicGroupQuestionItem findOne(ParentQuestionItemId parentQuestionItemId) {
        return postLoadProcessing(repository.findOne(parentQuestionItemId));
    }

    @Override
    public TopicGroupQuestionItem save(TopicGroupQuestionItem instance) {
        return postLoadProcessing(repository.save(instance));
    }

    @Override
    public List<TopicGroupQuestionItem> save(List<TopicGroupQuestionItem> instances) {
        return repository.save(instances).stream().
                map(q->postLoadProcessing(q)).
                collect(Collectors.toList());
    }

    @Override
    public void delete(ParentQuestionItemId parentQuestionItemId) throws RequestAbortedException {
        repository.delete(parentQuestionItemId);
    }

    @Override
    public void delete(List<TopicGroupQuestionItem> instances) {
        repository.delete(instances);
    }


    protected TopicGroupQuestionItem prePersistProcessing(TopicGroupQuestionItem instance) {
        return instance;
    }


    protected TopicGroupQuestionItem postLoadProcessing(TopicGroupQuestionItem instance) {
        instance.setQuestionItem(auditService.findRevision(
                instance.getId().getQuestionItemId(),
                instance.getQuestionItemRevision())
                .getEntity());
        return instance;
    }

}
