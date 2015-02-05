package no.nsd.qddt.service;

import no.nsd.qddt.domain.Agency;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AgencyService extends BaseService<Agency> {

    Agency findById(UUID id);

}
