package no.nsd.qddt.service;

import no.nsd.qddt.domain.Study;
import no.nsd.qddt.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("studyService")
public class StudyServiceImpl implements StudyService {

    private StudyRepository studyRepository;

    @Autowired
    public StudyServiceImpl(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Study findOne(Long id) {
        return studyRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Study> findAll() {
        return studyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public Study save(Study study) {
        study.setCreated(LocalDateTime.now());
        return studyRepository.save(study);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Study> findLastChange(Long id) {
        return studyRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Study>> findAllRevisionsPageable(Study study, int min, int max) {
        return studyRepository.findRevisions(study.getId(), new PageRequest(min, max));
    }
}
