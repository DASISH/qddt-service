package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface OtherMaterialService extends BaseService<OtherMaterial,UUID> {


    /**
     *
     * @param guid
     * @param pageable
     * @return
     */
    Page<OtherMaterial> findAllByTopicGroup(UUID  guid, Pageable pageable);



}
