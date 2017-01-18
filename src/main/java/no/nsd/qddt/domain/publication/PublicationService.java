package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface PublicationService extends BaseService<Publication, UUID> {

    Page<Publication> findAllPageable(Pageable pageable);
}
