package no.nsd.qddt.domain.report;

import net.logstash.logback.encoder.org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;


/**
 * @author Stig Norland
 */
@Service("reportService")
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public <S extends Report> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Report> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public Report findOne(Long aLong) {
        return reportRepository.findOne( aLong );
    }

    @Override
    public boolean exists(Long aLong) {
        return reportRepository.exists( aLong );
    }

    @Override
    public Iterable<Report> findAll() {
        return reportRepository.findAll();
    }

    @Override
    public Iterable<Report> findAll(Iterable<Long> longs) {
        return findAll( longs );
    }

    @Override
    public long count() {
        return reportRepository.count();
    }

    @Override
    public void delete(Long aLong) {
        throw new NotImplementedException( "delete" );
    }

    @Override
    public void delete(Report entity) {
        throw new NotImplementedException( "delete" );

    }

    @Override
    public void delete(Iterable<? extends Report> entities) {
        throw new NotImplementedException( "delete" );

    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException( "delete" );

    }


}
