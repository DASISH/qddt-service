package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterial;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface OtherMaterialRepository extends BaseRepository<OtherMaterial,UUID>, RevisionRepository<OtherMaterial, UUID, Integer> {

    Optional<OtherMaterial> findByOwnerIdAndOriginalName(UUID owner, String name);

    List<OtherMaterial> findByOwnerId(UUID owner);

}
