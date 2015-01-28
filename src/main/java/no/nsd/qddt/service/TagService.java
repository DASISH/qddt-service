package no.nsd.qddt.service;

import no.nsd.qddt.domain.HashTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stig Norland
 */
public interface TagService {

    List<HashTag> findAll();

    public Page<HashTag> findAll (Pageable pageable);

}
