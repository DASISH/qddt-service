package no.nsd.qddt.domain.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public long count() {
        return codeRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return codeRepository.exists(uuid);
    }

    @Override
    public Code findOne(UUID uuid) {
        return codeRepository.findOne(uuid);
    }

    @Override
    @Transactional(readOnly = false)
    public Code save(Code instance) {
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


    @Override
    public List<Code> findByResponseDomainId(UUID responseDomainId) {
        return codeRepository.findByResponseDomainId(responseDomainId);
    }

    @Override
    public List<Code> findByCategoryId(UUID codeId) {
        return codeRepository.findByCategoryId(codeId);
    }
}