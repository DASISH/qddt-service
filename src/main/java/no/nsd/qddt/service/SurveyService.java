package no.nsd.qddt.service;

import no.nsd.qddt.domain.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

import java.util.List;

public interface SurveyService {

    Survey findOne(Long id);

    List<Survey> findAll();

    Survey save(Survey survey);

    Revision<Integer, Survey> findLastChange(Long id);

    Page<Revision<Integer, Survey>> findAllRevisionsPageable(Survey survey, int min, int max);

}
