package no.nsd.qddt.domain.changefeed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface ChangeFeedRepository extends JpaRepository<ChangeFeed,ChangeFeedKey> {

    Page<ChangeFeed> findByNameLikeIgnoreCaseOrRefChangeKindLikeIgnoreCaseOrRefKindLikeIgnoreCase
        (String name, String changeKind, String kind, Pageable pageable);
}
