package no.nsd.qddt.domain.agency;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import no.nsd.qddt.domain.interfaces.BaseService;

/**
 * @author Stig Norland
 */
public interface AgencyService extends BaseService<Agency, UUID> {

    List<Agency> getAll();

    Page<Agency> findByNamePageable(String name, Pageable pageable);
}
