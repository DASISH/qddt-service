package no.nsd.qddt.domain.publicationstatus;

import no.nsd.qddt.domain.classes.interfaces.BaseService;

import java.util.List;

/**
 * @author Stig Norland
 */
public interface PublicationStatusService  extends BaseService<PublicationStatus, Long> {

    List<PublicationStatus> findAll();

}
