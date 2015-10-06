package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("surveyProgramService")
class SurveyProgramServiceImpl implements SurveyProgramService {

    private SurveyProgramRepository surveyProgramRepository;

    @Autowired
    public SurveyProgramServiceImpl(SurveyProgramRepository surveyProgramRepository) {
        this.surveyProgramRepository = surveyProgramRepository;
    }


    @Override
    public long count() {
        return surveyProgramRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return surveyProgramRepository.exists(uuid);
    }

    @Override
    public SurveyProgram findOne(UUID uuid) {
        return surveyProgramRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, SurveyProgram.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurveyProgram> findAll() {
        return surveyProgramRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SurveyProgram> findAll(Pageable pageable) {
        return surveyProgramRepository.findAll(pageable);
    }

    @Override
    public List<SurveyProgram> findAll(Iterable<UUID> uuids) {
        return surveyProgramRepository.findAll(uuids);
    }


    @Override
    @Transactional(readOnly = false)
    public SurveyProgram save(SurveyProgram instance) {
        return surveyProgramRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        surveyProgramRepository.delete(uuid);
    }
}