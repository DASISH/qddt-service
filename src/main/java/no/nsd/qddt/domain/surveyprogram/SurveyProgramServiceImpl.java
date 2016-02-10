package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

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
    @Transactional(readOnly = false)
    public SurveyProgram save(SurveyProgram instance) {

        SurveyProgram retval=null;
        try {
            retval= surveyProgramRepository.save(instance);
        }catch (Exception e){
            System.out.println("SAVING SURVEY FAILED ->" + e.getMessage());
        }
        return retval;
    }

    @Override
    @Transactional(readOnly = false)
    public List<SurveyProgram> save(List<SurveyProgram> instances) {
        return surveyProgramRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        surveyProgramRepository.delete(uuid);
    }

    @Override
    public void delete(List<SurveyProgram> instances) {
        surveyProgramRepository.delete(instances);
    }

    @Override
    public List<SurveyProgram> findByCreatedBy(User user) {
        return surveyProgramRepository.findByCreatedByOrderByCreatedAsc(user);
    }

    @Override
    public List<SurveyProgram> findByAgency(User user) {
        return surveyProgramRepository.findByAgencyOrderByCreatedAsc(user.getAgency());
    }
}