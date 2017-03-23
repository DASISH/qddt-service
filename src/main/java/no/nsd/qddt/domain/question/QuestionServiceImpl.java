package no.nsd.qddt.domain.question;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = false)
    public Question save(Question instance) {

        return questionRepository.save(instance);
    }

    @Override
    public List<Question> save(List<Question> instances) {
        return questionRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        questionRepository.delete(uuid);
    }

    @Override
    public void delete(List<Question> instances) {
        questionRepository.delete(instances);
    }

    @Override
    public Question prePersistProcessing(Question instance) {
        return instance;
    }

    @Override
    public Question postLoadProcessing(Question instance) {
        return instance;
    }

    @Override
    public Page<Question> getHierarchy(Pageable pageable) {
        return  questionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Question> findAllPageable(Pageable pageable){
        return questionRepository.findAll(pageable);
    }
	
}