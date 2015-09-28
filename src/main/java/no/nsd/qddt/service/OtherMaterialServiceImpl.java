package no.nsd.qddt.service;

import no.nsd.qddt.domain.OtherMaterial;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.OtherMaterialRepository;
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
public class OtherMaterialServiceImpl implements OtherMaterialService {

    private OtherMaterialRepository otherMaterialRepository;

    @Autowired
    OtherMaterialServiceImpl(OtherMaterialRepository otherMaterialRepository){
        this.otherMaterialRepository = otherMaterialRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<OtherMaterial> findAllByTopicGroup(UUID guid, Pageable pageable) {
        return otherMaterialRepository.findAllByOwnerGuid(guid, pageable);
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
    @Transactional(readOnly = true)
    public List<OtherMaterial> findAll() {
        return otherMaterialRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<OtherMaterial> findAll(Pageable pageable) {return otherMaterialRepository.findAll(pageable);  }

    @Override
    public List<OtherMaterial> findAll(Iterable<UUID> uuids) {
        return otherMaterialRepository.findAll(uuids);
    }


    @Override
    @Transactional(readOnly = false)
    public OtherMaterial save(OtherMaterial instance) {

        instance.setCreated(LocalDateTime.now());
        return otherMaterialRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        otherMaterialRepository.delete(uuid);
    }


}
