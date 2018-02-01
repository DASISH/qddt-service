package no.nsd.qddt.domain.othermaterial;

import no.nsd.qddt.domain.BaseRepository;
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


    // Tar denne med for å teste funksjonaliteten, den er strengt tatt unødvendig å ha med, da findAllByModule gir deg det du trenger.
//    public final static String FIND_BY_MODULE_BY_UUID_QUERY =
//            "SELECT A.* FROM OtherMaterial A " +
//                    " LEFT JOIN OtherMaterial B ON B.moduleid = A.moduleid " +
//                    " WHERE B.Guid = :guid";
//
//    public final static String COUNT_BY_MODULE_BY_UUID_QUERY =
//            "SELECT COUNT(*) FROM OtherMaterial A " +
//                    " LEFT JOIN OtherMaterial B ON B.moduleid = A.moduleid " +
//                    " WHERE B.Guid = :guid";


//    /**
//     * @param guid Is an UUID of an attachment that belongs to a module
//     * @param pageable Pageable object
//     * @return All attachments that belongs to a module
//     */
//    @NamedNativeQuery(value = FIND_BY_MODULE_BY_UUID_QUERY, countQuery = COUNT_BY_MODULE_BY_UUID_QUERY)
//    Page<Attachment> findAllByModuleGuid(@Param("guid") String guid, Pageable pageable);
//
//    /**
//     * @param id Is an topicGroupid.
//     * @param pageable Pageable object
//     * @return All attachments that belongs to the module with moduleId.
//     */
//    Page<OtherMaterial> findAllByTopicGroupId(UUID id, Pageable pageable);


}
