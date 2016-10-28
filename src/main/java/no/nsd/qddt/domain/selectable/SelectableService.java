package no.nsd.qddt.domain.selectable;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface SelectableService extends BaseService<Selectable, UUID> {

    Page<Selectable> findAllPageable(Pageable pageable);
}
