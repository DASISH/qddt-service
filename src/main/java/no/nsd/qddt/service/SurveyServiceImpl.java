package no.nsd.qddt.service;

import no.nsd.qddt.domain.Attachment;
import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.SurveyRepository;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
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
                () -> new ResourceNotFoundException(id, Survey.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Survey> findAll() {
        return surveyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Survey> findAll(Pageable pageable) {
        return surveyRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = false)
    public Survey save(Survey instance) {

        instance.setCreated(LocalDateTime.now());
        return surveyRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Survey instance) {
        surveyRepository.delete(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public Survey findById(UUID id) {
        throw new NotImplementedException();
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

    @Override
    public Revision<Integer, Survey> findEntityAtRevision(Long id, Integer revision) {
        return surveyRepository.findEntityAtRevision(id, revision);
    }
}