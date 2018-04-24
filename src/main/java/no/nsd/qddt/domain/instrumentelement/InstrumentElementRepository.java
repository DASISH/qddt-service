package no.nsd.qddt.domain.instrumentelement;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface InstrumentElementRepository extends BaseRepository<InstrumentElement,UUID> {

}