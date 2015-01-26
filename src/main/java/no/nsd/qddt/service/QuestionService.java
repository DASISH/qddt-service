package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionService {

    public Question findById(Long id);

    public List<Question> findAll();

    public Question save(Question question);

    public void delete(Question question);

    public Revision<Integer, Question> findLastChange(Long id);

    public Page<Revision<Integer, Question>> findAllRevisionsPageable(Question question, int min, int max);
}
