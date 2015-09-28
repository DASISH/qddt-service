package no.nsd.qddt.service;

import no.nsd.qddt.domain.SurveyProgram;
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
    public long count() {
        return surveyRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return surveyRepository.exists(uuid);
    }

    @Override
    public SurveyProgram findOne(UUID uuid) {
        return surveyRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, SurveyProgram.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurveyProgram> findAll() {
        return surveyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SurveyProgram> findAll(Pageable pageable) {
        return surveyRepository.findAll(pageable);
    }

    @Override
    public List<SurveyProgram> findAll(Iterable<UUID> uuids) {
        return surveyRepository.findAll(uuids);
    }


    @Override
    @Transactional(readOnly = false)
    public SurveyProgram save(SurveyProgram instance) {

        instance.setCreated(LocalDateTime.now());
        return surveyRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        surveyRepository.delete(uuid);
    }


    @Override
    public Revision<Integer, SurveyProgram> findLastChange(UUID uuid) {
        return surveyRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, SurveyProgram> findEntityAtRevision(UUID uuid, Integer revision) {
        return surveyRepository.findEntityAtRevision(uuid,revision);
    }

    @Override
    public Page<Revision<Integer, SurveyProgram>> findAllRevisionsPageable(UUID uuid, Pageable pageable) {
        return surveyRepository.findRevisions(uuid,pageable);
    }
}