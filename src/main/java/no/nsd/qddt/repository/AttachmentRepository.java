package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface AttachmentRepository extends BaseRepository<Attachment>, EnversRevisionRepository<Attachment, UUID, Integer> {


    // Tar denne med for å teste funksjonaliteten, den er strengt tatt unødvendig å ha med, da findAllByModule gir deg det du trenger.
    public final static String FIND_BY_MODULE_BY_UUID_QUERY =
            "SELECT A.* FROM Attachment A " +
                    " LEFT JOIN Attachment B ON B.moduleid = A.moduleid " +
                    " WHERE B.Guid = :guid";

    public final static String COUNT_BY_MODULE_BY_UUID_QUERY =
            "SELECT COUNT(*) FROM Attachment A " +
                    " LEFT JOIN Attachment B ON B.moduleid = A.moduleid " +
                    " WHERE B.Guid = :guid";


    // TODO fix this query?
//    /**
//     * @param guid Is an UUID of an attachment that belongs to a module
//     * @param pageable Pageable object
//     * @return All attachments that belongs to a module
//     */
//    @Query(value = FIND_BY_MODULE_BY_UUID_QUERY, countQuery = COUNT_BY_MODULE_BY_UUID_QUERY)
//    Page<Attachment> findAllByModuleGuid(@Param("guid") String guid, Pageable pageable);

    /**
     * @param moduleId Is an moduleId.
     * @param pageable Pageable object
     * @return All attachments that belongs to the module with moduleId.
     */
    Page<Attachment> findAllByModule(Long moduleId, Pageable pageable);


}
