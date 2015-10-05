package no.nsd.qddt.domain.conceptquestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("conceptQuestionService")
public class ConceptQuestionServiceImpl implements ConceptQuestionService{

    private ConceptQuestionRepository conceptQuestionRepository;

    @Autowired
    public ConceptQuestionServiceImpl(ConceptQuestionRepository conceptQuestionRepository){
        this.conceptQuestionRepository = conceptQuestionRepository;
    }

    @Override
    public Revision<Integer, ConceptQuestion> findLastChange(UUID uuid) {
        return conceptQuestionRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, ConceptQuestion> findEntityAtRevision(UUID uuid, Integer revision) {
        return conceptQuestionRepository.findEntityAtRevision(uuid,revision);
    }

    @Override
    public Page<Revision<Integer, ConceptQuestion>> findAllRevisionsPageable(UUID uuid, Pageable pageable) {
        return conceptQuestionRepository.findRevisions(uuid,pageable);
    }

    @Override
    public long count() {
        return conceptQuestionRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return conceptQuestionRepository.exists(uuid);
    }

    @Override
    public ConceptQuestion findOne(UUID uuid) {
        return conceptQuestionRepository.findOne(uuid);
    }

    @Override
    public List<ConceptQuestion> findAll() {
        return conceptQuestionRepository.findAll();
    }

    @Override
    public Page<ConceptQuestion> findAll(Pageable pageable) {
        return conceptQuestionRepository.findAll(pageable);
    }

    @Override
    public List<ConceptQuestion> findAll(Iterable<UUID> uuids) {
        return conceptQuestionRepository.findAll(uuids);
    }

    @Override
    public ConceptQuestion save(ConceptQuestion instance) {
        return conceptQuestionRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        conceptQuestionRepository.delete(uuid);
    }
}
