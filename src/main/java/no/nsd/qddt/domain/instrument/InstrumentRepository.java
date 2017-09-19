package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface InstrumentRepository extends BaseRepository<Instrument,UUID> {

    List<Instrument> findByStudies(UUID studyId);

    Page<Instrument> findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(String name, String description, Pageable pageable);
}

