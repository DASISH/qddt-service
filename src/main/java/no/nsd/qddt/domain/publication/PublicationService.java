package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.embedded.ElementRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface PublicationService extends BaseService<Publication, UUID> {

    Page<Publication> findAllPageable(Pageable pageable);

    Page<Publication> findByNameOrPurposeAndStatus(String name, String purpose, String status, Pageable pageable);

    ElementRef getDetail(ElementRef publicationElement);

}
