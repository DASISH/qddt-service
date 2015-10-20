package no.nsd.qddt.domain.code;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
interface CodeRepository extends BaseRepository<Code,UUID> {

    List<Code> findByNameIgnoreCaseContains(String tags);

    Page<Code> findByNameIgnoreCaseContains(String[] tags, Pageable pageable);

//    List<String> findAllCategory();
//
//    Page<String> findAllCategory(Pageable pageable);
}