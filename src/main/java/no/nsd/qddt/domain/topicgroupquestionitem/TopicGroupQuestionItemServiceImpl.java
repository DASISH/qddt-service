package no.nsd.qddt.domain.topicgroupquestionitem;

import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.exception.RequestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
@Service("topicGroupQuestionItemService")
public class TopicGroupQuestionItemServiceImpl implements TopicGroupQuestionItemService {

    private final TopicGroupQuestionItemRepository repository;
    private final QuestionItemAuditService auditService;

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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public TopicGroupQuestionItem save(TopicGroupQuestionItem instance) {
        return postLoadProcessing(repository.save(instance));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public Set<TopicGroupQuestionItem> save(Set<TopicGroupQuestionItem> instances) {
        return repository.save(instances).stream().
                map(this::postLoadProcessing).
                collect(Collectors.toSet());
    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public List<TopicGroupQuestionItem> save(List<TopicGroupQuestionItem> instances) {
        return repository.save(instances).stream().
                map(this::postLoadProcessing).
                collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public void delete(ParentQuestionItemId parentQuestionItemId) throws RequestAbortedException {
        repository.delete(parentQuestionItemId);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public void delete(List<TopicGroupQuestionItem> instances) {
        repository.delete(instances);
    }


    protected TopicGroupQuestionItem prePersistProcessing(TopicGroupQuestionItem instance) {
        return instance;
    }


    private TopicGroupQuestionItem postLoadProcessing(TopicGroupQuestionItem instance) {
        instance.setQuestionItem(auditService.findRevision(
                instance.getId().getQuestionItemId(),
                instance.getQuestionItemRevision().intValue())
                .getEntity());
        return instance;
    }

}
