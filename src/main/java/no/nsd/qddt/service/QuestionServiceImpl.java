package no.nsd.qddt.service;

import no.nsd.qddt.domain.Comment;
import no.nsd.qddt.domain.Question;
import no.nsd.qddt.repository.CommentRepository;
import no.nsd.qddt.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public Question findById(Long id) {
        return questionRepository.findOne(id);
    }

    @Override
    public List<Question> findAll() {
        return null;
    }

    @Override
    public Question save(Question question) {
        question.setCreated(LocalDateTime.now());
        return questionRepository.save(question);
    }

    @Override
    public void delete(Question question) {
        questionRepository.delete(question);
    }
}
