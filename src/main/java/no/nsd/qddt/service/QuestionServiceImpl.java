package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;
import no.nsd.qddt.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("questionService")
public class QuestionServiceImpl implements QuestionService{

    private QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Question question) {
        questionRepository.delete(question);
    }
}
