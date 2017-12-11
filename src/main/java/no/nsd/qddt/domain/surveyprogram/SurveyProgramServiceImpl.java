package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("surveyProgramService")
class SurveyProgramServiceImpl implements SurveyProgramService {

    private final SurveyProgramRepository surveyProgramRepository;

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
        return surveyProgramRepository.existsById(uuid);
    }

    @Override
    public SurveyProgram findOne(UUID uuid) {
        return surveyProgramRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, SurveyProgram.class)
        );
    }


    @Override
    @Transactional()
    public SurveyProgram save(SurveyProgram instance) {

        SurveyProgram retval=null;
        try {
            retval= postLoadProcessing(
                    surveyProgramRepository.save(
                            prePersistProcessing(instance)));
        }catch (Exception e){
            System.out.println("SAVING SURVEY FAILED ->" + e.getMessage());
        }
        return retval;
    }

//    @Override
//    @Transactional()
//    public List<SurveyProgram> save(List<SurveyProgram> instances) {
//        return surveyProgramRepository.save(instances);
//    }

    @Override
    public void delete(UUID uuid) {
        surveyProgramRepository.deleteById(uuid);
    }

    @Override
    public void delete(List<SurveyProgram> instances) {
        surveyProgramRepository.deleteAll(instances);
    }

    private SurveyProgram prePersistProcessing(SurveyProgram instance) {
        return instance;
    }

    private SurveyProgram postLoadProcessing(SurveyProgram instance) {
//        System.out.println("comments : " + instance.getComments().size());
        return instance;
    }

    @Override
    public List<SurveyProgram> findByModifiedBy(User user) {
        return surveyProgramRepository.findByModifiedByOrderByModifiedAsc(user);
    }

    @Override
    public List<SurveyProgram> findByAgency(User user) {
        return surveyProgramRepository.findByAgencyOrderByModifiedAsc(user.getAgency())
                .stream().map(this::postLoadProcessing).collect(Collectors.toList());
    }
}