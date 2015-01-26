package no.nsd.qddt.service;

import no.nsd.qddt.domain.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface SurveyService {

    public Survey findOne(Long id);

    public List<Survey> findAll();

    public Survey save(Survey survey);

    public Revision<Integer, Survey> findLastChange(Long id);

    public Page<Revision<Integer, Survey>> findAllRevisionsPageable(Survey survey, int min, int max);

}
