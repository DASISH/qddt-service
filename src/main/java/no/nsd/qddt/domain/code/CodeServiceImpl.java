package no.nsd.qddt.domain.code;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
        return codeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return codeRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Code findOne(UUID uuid) {
        return codeRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Code.class)
        );
    }


    @Override
    @Transactional(readOnly = false)
    public Code save(Code instance) {

        instance.setCreated(LocalDateTime.now());
        return codeRepository.save(instance);
    }

    @Override
    public List<Code> save(List<Code> instances) {
        return codeRepository.save(instances);
    }


    @Override
    public void delete(UUID uuid) {
        codeRepository.delete(uuid);
    }

    @Override
    public void delete(List<Code> instances) {
        codeRepository.delete(instances);
    }
}
