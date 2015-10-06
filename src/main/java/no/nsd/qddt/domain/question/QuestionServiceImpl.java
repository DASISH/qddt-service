package no.nsd.qddt.domain.question;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("questionService")
class QuestionServiceImpl implements QuestionService {


    private QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public long count() {
        return questionRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return questionRepository.exists(uuid);
    }

    @Override
    public Question findOne(UUID uuid) {
        return questionRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Question.class)
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
    public List<Question> findAll(Iterable<UUID> uuids) {
        return questionRepository.findAll(uuids);
    }

    @Override
    @Transactional(readOnly = false)
    public Question save(Question instance) {

        instance.setCreated(LocalDateTime.now());
        return questionRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        questionRepository.delete(uuid);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Question> findByParentPageable(UUID guidId, Pageable pageable) {
        Question child= questionRepository.findOne(guidId);
        return questionRepository.findAllByParentId(child.getParent().getId(), pageable);
    }
}