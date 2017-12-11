package no.nsd.qddt.domain.study;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
        return studyRepository.existsById(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Study findOne(UUID uuid) {
        return studyRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Study.class)
        );    }


    @Override
    @Transactional()
    public Study save(Study instance) {
        return postLoadProcessing(
                studyRepository.save(
                        prePersistProcessing(instance)));
    }

//    @Override
//    public List<Study> save(List<Study> instances) {
//        return studyRepository.save(instances);
//    }

    @Override
    @Transactional()
    public void delete(UUID uuid) {

        studyRepository.deleteById(uuid);
    }

    @Override
    public void delete(List<Study> instances) {
        studyRepository.deleteAll(instances);
    }

    private Study prePersistProcessing(Study instance) {
        try {
            if (instance.getChangeKind() == AbstractEntityAudit.ChangeKind.ARCHIVED) {
                String changecomment =  instance.getChangeComment();
                instance = findOne(instance.getId());
                instance.setArchived(true);
                instance.setChangeComment(changecomment);
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

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
