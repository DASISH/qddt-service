package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
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
        return surveyProgramRepository.exists(uuid);
    }

    @Override
    public SurveyProgram findOne(UUID uuid) {
        return surveyProgramRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, SurveyProgram.class)
        );
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public SurveyProgram save(SurveyProgram instance) {

        SurveyProgram retval=null;
        try {
            retval= postLoadProcessing(
                    surveyProgramRepository.save(
                            prePersistProcessing(instance)));
        }catch (Exception ex){
            LOG.error("SAVING SURVEY FAILED ->",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
        return retval;
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public List<SurveyProgram> save(List<SurveyProgram> instances) {
        return surveyProgramRepository.save(instances);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public void delete(UUID uuid) {
        surveyProgramRepository.delete(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public void delete(List<SurveyProgram> instances) {
        surveyProgramRepository.delete(instances);
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