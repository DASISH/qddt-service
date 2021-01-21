package no.nsd.qddt.domain.changefeed;

import no.nsd.qddt.domain.classes.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Stig Norland
 */
public interface ChangeFeedService  extends BaseService<ChangeFeed, ChangeFeedKey> {
    Page<ChangeFeed> findAllPageable(Pageable pageable);

    Page<ChangeFeed> filterbyPageable(String name, String change, String kind, Pageable pageable);
}
