package no.nsd.qddt.domain.conceptquestionitem;

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
public class ConceptQuestionItemServiceImpl implements ConceptQuestionItemService {

    private ConceptQuestionItemRepository repository;
    private QuestionItemAuditService auditService;

    @Autowired
    public ConceptQuestionItemServiceImpl(ConceptQuestionItemRepository repository, QuestionItemAuditService questionItemAuditService) {
        this.repository = repository;
        this.auditService = questionItemAuditService;
    }


    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean exists(ConceptQuestionItemId conceptQuestionItemId) {
        return repository.exists(conceptQuestionItemId);
    }

    @Override
    public ConceptQuestionItem findOne(ConceptQuestionItemId conceptQuestionItemId) {
        return postLoadProcessing(repository.findOne(conceptQuestionItemId));
    }

    @Override
    public ConceptQuestionItem save(ConceptQuestionItem instance) {
        return postLoadProcessing(repository.save(instance));
    }

    @Override
    public List<ConceptQuestionItem> save(List<ConceptQuestionItem> instances) {
        return repository.save(instances).stream().
                map(q->postLoadProcessing(q)).
                collect(Collectors.toList());
    }

    @Override
    public void delete(ConceptQuestionItemId conceptQuestionItemId) throws RequestAbortedException {
        repository.delete(conceptQuestionItemId);
    }

    @Override
    public void delete(List<ConceptQuestionItem> instances) {
        repository.delete(instances);
    }


    protected ConceptQuestionItem prePersistProcessing(ConceptQuestionItem instance) {
        return instance;
    }


    protected ConceptQuestionItem postLoadProcessing(ConceptQuestionItem instance) {
        instance.setQuestionItem(auditService.findRevision(
                instance.getId().getQuestionItemId(),
                instance.getQuestionItemRevision())
                .getEntity());
        return instance;
    }

}
