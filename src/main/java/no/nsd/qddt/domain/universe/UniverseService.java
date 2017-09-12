package no.nsd.qddt.domain.universe;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface UniverseService extends BaseService<Universe, UUID> {

    Page<Universe> findByDescriptionLike(String description, Pageable pageable);
}
