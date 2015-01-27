package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends RevisionRepository<Survey, Long, Integer>, JpaRepository<Survey, Long> {

    Optional<Survey> findById(Long id);
}
