package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;

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
}
