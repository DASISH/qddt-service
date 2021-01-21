package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.classes.interfaces.BaseArchivedRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Repository
interface SurveyProgramRepository extends BaseArchivedRepository<SurveyProgram,UUID> {

    List<SurveyProgram> findByAgencyOrderByAgencyIdx(Agency agency);


    @Query(name ="findByAgencyOrIsArchived", nativeQuery = true,
        value = "SELECT * FROM survey_program WHERE agency_id = :id OR is_archived = :isArchived ORDER BY agency_id, agency_idx")
    List<SurveyProgram> findByAgencyOrIsArchived(@Param("id")UUID id,@Param("isArchived")boolean isArchived);

    @Modifying()
    @Query(name ="reOrder", nativeQuery = true,
        value = "UPDATE survey_program SET agency_idx=:index WHERE id = :uuid")
    void reOrder(@Param("uuid")UUID uuid, @Param("index")Long index);
}
