package no.nsd.qddt.domain.code;

import no.nsd.qddt.exception.ResourceNotFoundException;
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
@Service("codeService")
class CodeServiceImpl implements CodeService {

    private CodeRepository codeRepository;

    @Autowired
    public CodeServiceImpl(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Code> findByHashTag(String tag) {
        return codeRepository.findByNameIgnoreCaseContains(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Code findOne(UUID uuid) {
        return codeRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Code.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Code> findAll() {
        return codeRepository.findAll();
    }

    @Override
    public Page<Code> findAll(Pageable pageable) {
        return codeRepository.findAll(pageable);
    }

    @Override
    public List<Code> findAll(Iterable<UUID> uuids) {
        return codeRepository.findAll(uuids);
    }


    @Override
    @Transactional(readOnly = false)
    public Code save(Code instance) {

        instance.setCreated(LocalDateTime.now());
        return codeRepository.save(instance);
    }


    @Override
    public void delete(UUID uuid) {
        codeRepository.delete(uuid);
    }


    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Code> findLastChange(UUID uuid) {
        return codeRepository.findLastChangeRevision(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Code> findEntityAtRevision(UUID uuid, Integer revision) {
        return codeRepository.findRevision(uuid, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Code>> findAllRevisionsPageable(UUID uuid, Pageable pageable) {
        return codeRepository.findRevisions(uuid,pageable);
    }
}
