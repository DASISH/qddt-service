package no.nsd.qddt.domain.study;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("studyService")
class StudyServiceImpl implements StudyService {

    private final StudyRepository studyRepository;

    @Autowired
    public StudyServiceImpl(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public long count() {
        return studyRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return studyRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Study findOne(UUID uuid) {
        return studyRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Study.class)
        );    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public Study save(Study instance) {
        return postLoadProcessing(
                studyRepository.save(
                        prePersistProcessing(instance)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public List<Study> save(List<Study> instances) {
        return studyRepository.save(instances);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(UUID uuid) {

        studyRepository.delete(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(List<Study> instances) {
        studyRepository.delete(instances);
    }

    private Study prePersistProcessing(Study instance) {

        instance = doArchive( instance ) ;

        if (instance.getTopicGroups() != null & !instance.isArchived())
            instance.getTopicGroups().forEach(c->{
                c.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_PARENT);
                c.setChangeComment("");
            });
        return instance;
    }




    private Study postLoadProcessing(Study instance) {
        return instance;
    }
}
