package no.nsd.qddt.service;

import no.nsd.qddt.domain.Study;
import no.nsd.qddt.repository.StudyRepository;
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
@Service("studyService")
public class StudyServiceImpl implements StudyService {

    private StudyRepository studyRepository;

    @Autowired
    public StudyServiceImpl(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Study findById(Long id) { return studyRepository.findOne(id);   }

    @Override
    @Transactional(readOnly = true)
    public List<Study> findAll() {
        return studyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Study> findAllPageable(Pageable pageable) {
        return studyRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public Study save(Study instance) {

        instance.setCreated(LocalDateTime.now());
        return studyRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Study instance) {
        studyRepository.delete(instance);
    }

    @Override
    @Transactional(readOnly = true)
    public Study findById(UUID id) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Study> findLastChange(Long id) {
        return studyRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Study> findEntityAtRevision(Long id, Integer revision) {
        return studyRepository.findEntityAtRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Study>> findAllRevisionsPageable(Long id, Pageable pageable) {
        return studyRepository.findRevisions(id, pageable);
    }
}
