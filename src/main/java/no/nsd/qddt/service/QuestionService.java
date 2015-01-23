package no.nsd.qddt.service;

import no.nsd.qddt.domain.Question;

/**
 * @author Stig Norland
 */
public interface QuestionService {

    public Question findById(Long id);

    public Question save(Question comment);

    public void delete(Question comment);
}
