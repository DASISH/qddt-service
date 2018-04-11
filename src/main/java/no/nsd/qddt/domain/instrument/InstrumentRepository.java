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

    List<Instrument> findByStudy(UUID studyId);

    Page<Instrument> findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseOrInstrumentKind(String name, String description, InstrumentKind InstrumentKind,  Pageable pageable);
}

