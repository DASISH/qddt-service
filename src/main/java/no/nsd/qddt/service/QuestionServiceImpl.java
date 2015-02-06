package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("questionService")
public class QuestionServiceImpl implements QuestionService {


    private QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Question.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Question> findAll(Pageable pageable) { return questionRepository.findAll(pageable);   }

    @Override
    @Transactional(readOnly = false)
    public Question save(Question instance) {

        instance.setCreated(LocalDateTime.now());
        return questionRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Question instance) {
        questionRepository.delete(instance);
    }

    @Override
    @Transactional(readOnly = true)
    public Question findByGuid(UUID id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Question> findLastChange(Long id) {
        return questionRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Question> findEntityAtRevision(Long id, Integer revision) {
        return questionRepository.findEntityAtRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Question>> findAllRevisionsPageable(Long id, Pageable pageable) {
        return questionRepository.findRevisions(id,pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Question> findByParentPageable(Long parentId, Pageable pageable) {
        return questionRepository.findAllByParent(parentId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Question> findByParentPageable(UUID guidId, Pageable pageable) {
        // funker dette?
        return questionRepository.findAllByParent(findByGuid(guidId).getParent().getId(), pageable);
    }

//    @Override
//    public Page<Question> findQuestionConceptPageable(Long id, Pageable pageable) {
//        return questionRepository.findQuestionConcept(id,pageable);
//    }

//    @Override
//    public Page<Question> findQuestionInstrument(Long id, Pageable pageable) {
//        return questionRepository.findQuestionInstrument(id,pageable);
//    }
}
