package no.nsd.qddt.domain.publicationstatus;

import no.nsd.qddt.domain.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface PublicationStatusService  extends BaseService<PublicationStatus, UUID> {

    List<PublicationStatus> findAll();

}
