package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.classes.elementref.ElementRefEmbedded;
import no.nsd.qddt.domain.classes.interfaces.BaseService;
import no.nsd.qddt.domain.classes.interfaces.IElementRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface PublicationService extends BaseService<Publication, UUID> {

    Page<Publication> findByNameOrPurposeAndStatus(String name, String purpose, String publicationStatus, String publishedKind,  Pageable pageable);

    ElementRefEmbedded getDetail(IElementRef publicationElement);

}
