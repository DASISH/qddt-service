package no.nsd.qddt.service;

import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.exception.SurveyNotFoundException;
import no.nsd.qddt.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("surveyService")
public class SurveyServiceImpl implements SurveyService {

    private SurveyRepository surveyRepository;

    @Autowired
    public SurveyServiceImpl(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Survey findById(Long id) {
        return surveyRepository.findById(id).orElseThrow(
                () -> new SurveyNotFoundException(String.valueOf(id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Survey> findAll() {
        return surveyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public Survey save(Survey survey) {
        survey.setCreated(LocalDateTime.now());
        return surveyRepository.save(survey);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Survey> findLastChange(Long id) {
        return surveyRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Survey>> findAllRevisionsPageable(Long id, Pageable pageable) {
        return surveyRepository.findRevisions(id, pageable);
    }
}