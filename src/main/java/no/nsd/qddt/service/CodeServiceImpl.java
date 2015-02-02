package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.Code;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.CodeRepository;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("codeService")
public class CodeServiceImpl implements CodeService {

    private CodeRepository codeRepository;

    @Autowired
    public CodeServiceImpl(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Code findById(Long id) {
        return codeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Code.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    //TODO implement findby uuid
    public Code findById(UUID id) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Code> findByHashTag(String tag) {
        return codeRepository.findByNameIgnoreCaseContains(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Code> findAll() {
        return codeRepository.findAll();
    }

    @Override
    public Page<Code> findAllPageable(Pageable pageable) {
        return codeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public Code save(Code instance) {

        instance.setCreated(LocalDateTime.now());
        return codeRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Code instance) {
        codeRepository.delete(instance);
    }


    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Code> findLastChange(Long id) {
        return codeRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Code> findEntityAtRevision(Long id, Integer revision) {
        return codeRepository.findEntityAtRevision(id, revision);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Code>> findAllRevisionsPageable(Long id, Pageable pageable) {
        return codeRepository.findRevisions(id, pageable);
    }
}
