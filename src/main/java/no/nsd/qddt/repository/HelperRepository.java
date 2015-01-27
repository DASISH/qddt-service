package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;

/**
 * @author Stig Norland
 */

public interface  HelperRepository  extends RevisionRepository<String, Long, Integer>, JpaRepository<String, Long> {


    @Query("select distinct name from Code")
    List<String> findDistinctByName();

}

