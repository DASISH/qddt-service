package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface AttachmentRepository extends BaseRepository<Attachment> {


    // Tar denne med for å teste funksjonaliteten, den er strengt tatt unødvendig å ha med, da findAllByModule gir deg det du trenger.
    public final static String FIND_BY_MODULE_BY_UUID_QUERY =
            "SELECT A.* FROM Attachment A " +
                    " LEFT JOIN Attachment B ON B.moduleid = A.moduleid " +
                    " WHERE B.Guid = ?1";


    @Query (FIND_BY_MODULE_BY_UUID_QUERY)
    /**
     * @param guid Is an UUID of an attachment that belongs to a module
     * @param pageable
     * @return All attachments that belongs to a module
     */
    Page<Attachment> findAllByModuleGuid( String guid, Pageable pageable);

    /**
     * @param moduleId Is an moduleId.
     * @param pageable
     * @return All attachments that belongs to the module with moduleId.
     */
    Page<Attachment> findAllByModule(Long moduleId, Pageable pageable);


}
