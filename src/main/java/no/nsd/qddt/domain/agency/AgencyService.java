package no.nsd.qddt.domain.agency;

import no.nsd.qddt.domain.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AgencyService extends BaseService<Agency, UUID> {

    List<Agency> getAll();
}
