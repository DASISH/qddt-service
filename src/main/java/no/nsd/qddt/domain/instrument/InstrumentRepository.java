package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.instrument.pojo.Instrument;
import no.nsd.qddt.classes.interfaces.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
interface InstrumentRepository extends BaseRepository<Instrument,UUID> {

    List<Instrument> findByStudy(UUID studyId);

//    Page<Instrument> findByXmlLangLikeAndLabelLikeIgnoreCaseOrDescriptionLikeIgnoreCaseOrInstrumentKind(String xmlLang, String name, String description, InstrumentKind InstrumentKind, Pageable pageable);

    @Query(value = "SELECT ins.* FROM public.instrument ins WHERE " +
        "(ins.xml_lang ILIKE :xmlLang) AND (ins.instrument_kind in (:instrumentKinds)) " +
        "AND (ins.name ILIKE :name or ins.label ILIKE :name  or ins.description ILIKE :description ) " +
        "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(ins.*) FROM public.instrument ins WHERE " +
        "(ins.xml_lang ILIKE :xmlLang) AND (ins.instrument_kind in (:instrumentKinds)) " +
        "AND (ins.name ILIKE :name or ins.label ILIKE :name  or ins.description ILIKE :description ) " +
        "ORDER BY ?#{#pageable}"
        ,nativeQuery = true)
    Page<Instrument> findByQueryAndKinds(@Param("name")String name,
                                         @Param("description")String description,
                                         @Param("instrumentKinds")List<String> instrumentKinds,
                                         @Param("xmlLang")String xmlLang,
                                         Pageable pageable);


    @Query(value = "SELECT ins.* FROM public.instrument ins WHERE " +
        "ins.xml_lang ILIKE :xmlLang AND (ins.name ILIKE :name or ins.label ILIKE :name  or ins.description ILIKE :description ) " +
        "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(ins.*) FROM public.instrument ins WHERE " +
        "ins.xml_lang ILIKE :xmlLang AND (ins.name ILIKE :name or ins.label ILIKE :name  or ins.description ILIKE :description ) "
        + " ORDER BY ?#{#pageable}"
        ,nativeQuery = true)
    Page<Instrument> findByQuery(@Param("name")String name,
                                 @Param("description")String description,
                                 @Param("xmlLang")String xmlLang,
                                 Pageable pageable);
}

