package no.nsd.qddt.service;

import no.nsd.qddt.domain.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface SurveyService {

    public Survey findById(Long id);

    public List<Survey> findAll();

    public Survey save(Survey survey);

    public Revision<Integer, Survey> findLastChange(Long id);

    public Page<Revision<Integer, Survey>> findAllRevisionsPageable(Long id,Pageable pageable);

}
