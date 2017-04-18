package no.nsd.qddt.domain.conceptquestionitem;

import no.nsd.qddt.exception.RequestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("conceptQuestionItemService")
public class ConceptQuestionItemServiceImpl implements ConceptQuestionItemService {

    private ConceptQuestionItemRepository repository;

    @Autowired
    public ConceptQuestionItemServiceImpl(ConceptQuestionItemRepository repository) {
        this.repository = repository;
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
        return repository.findOne(conceptQuestionItemId);
    }

    @Override
    public <S extends ConceptQuestionItem> S save(S instance) {
        return repository.save(instance);
    }

    @Override
    public List<ConceptQuestionItem> save(List<ConceptQuestionItem> instances) {
        return repository.save(instances);
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
        return instance;
    }

}
