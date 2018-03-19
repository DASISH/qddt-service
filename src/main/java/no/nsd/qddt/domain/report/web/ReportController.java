package no.nsd.qddt.domain.report.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.report.Report;
import no.nsd.qddt.domain.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/report")
public class ReportController extends AbstractController {

    private final ReportService reportService;


    @Autowired
    public ReportController(ReportService reportService
    ) {
        this.reportService = reportService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/all", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public Iterable<Report> getAll() {
        return  reportService.findAll();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/report/{id}", method = RequestMethod.GET)
    public Report getReport(@PathVariable("id") Long id) {
        return  reportService.findOne( id );

    }


}
