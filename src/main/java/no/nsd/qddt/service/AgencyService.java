package no.nsd.qddt.service;

import no.nsd.qddt.domain.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Stig Norland
 */
public interface AgencyService {

    Agency findById(Long id);

    List<Agency> findAll();

    Agency save(Agency agency);

    public Revision<Integer, Agency> findLastChange(Long id);

    public Page<Revision<Integer, Agency>> findAllRevisionsPageable(Agency agency, Pageable pageable);

}
