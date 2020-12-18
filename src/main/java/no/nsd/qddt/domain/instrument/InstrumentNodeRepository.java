package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.instrument.pojo.InstrumentNode;
import no.nsd.qddt.classes.interfaces.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface InstrumentNodeRepository extends BaseRepository<InstrumentNode<?>, UUID> {

}

