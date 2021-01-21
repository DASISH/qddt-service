package no.nsd.qddt.domain.universe;

import no.nsd.qddt.domain.classes.interfaces.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface UniverseRepository extends BaseRepository<Universe, UUID> {

    Page<Universe> findByDescriptionIgnoreCaseLikeAndXmlLangLike(String description, String xmlLang,  Pageable pageable);


}

