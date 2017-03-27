package no.nsd.qddt.domain.conceptquestionitem;

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
    public boolean exists(UUID uuid) {
        return repository.exists(uuid);
    }

    @Override
    public ConceptQuestionItem findOne(UUID uuid) {
        return repository.findOne(uuid);
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
    public void delete(UUID uuid) {
        repository.delete(uuid);
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

    @Override
    public List<ConceptQuestionItem> findByConceptQuestionItem(UUID conceptId, UUID questionItemId) {
        return repository.findByConceptIdAndQuestionItemId(conceptId, questionItemId);
    }
}
