package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("otherMaterialService")
class OtherMaterialServiceImpl implements OtherMaterialService {

    private OtherMaterialRepository otherMaterialRepository;

    @Autowired
    OtherMaterialServiceImpl(OtherMaterialRepository otherMaterialRepository){
        this.otherMaterialRepository = otherMaterialRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<OtherMaterial> findAllByTopicGroup(UUID guid, Pageable pageable) {
        return otherMaterialRepository.findAllByTopicGroupId(guid, pageable);
    }


    @Override
    public long count() {
        return otherMaterialRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return otherMaterialRepository.exists(uuid);
    }

    @Override
    public OtherMaterial findOne(UUID uuid) {
        return otherMaterialRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, OtherMaterial.class)
        );
    }

    @Override
    @Transactional(readOnly = false)
    public OtherMaterial save(OtherMaterial instance) {

        instance.setCreated(LocalDateTime.now());
        return otherMaterialRepository.save(instance);
    }

    @Override
    public List<OtherMaterial> save(List<OtherMaterial> instances) {
        return otherMaterialRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        otherMaterialRepository.delete(uuid);
    }

    @Override
    public void delete(List<OtherMaterial> instances) {
        otherMaterialRepository.delete(instances);
    }


}
