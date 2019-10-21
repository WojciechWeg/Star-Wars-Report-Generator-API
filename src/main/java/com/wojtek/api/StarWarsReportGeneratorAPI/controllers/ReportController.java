package com.wojtek.api.StarWarsReportGeneratorAPI.controllers;

import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.ReportQuery;
import com.wojtek.api.StarWarsReportGeneratorAPI.reposotories.ReportRepository;
import com.wojtek.api.StarWarsReportGeneratorAPI.services.ReportService;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ReportController.BASE_URL)
public class ReportController {

    public static final String BASE_URL = "report";

    private final ReportService reportService;
    private final ReportRepository reportRepository;

    public ReportController(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    @GetMapping
    public List getListOfReports(){
        return (List) reportRepository.findAll();

    }

    @GetMapping({"/{report_id}"})
    public Report getReport(@PathVariable Long report_id) throws NotFound {

        return reportRepository.findById(report_id).orElseThrow(NotFound::new);

    }

    @DeleteMapping
    public void deleteAllReports(){
         reportRepository.deleteAll();
    }

    @DeleteMapping({"/{report_id}"})
    public void deleteReportById(@PathVariable Long report_id){
        reportRepository.deleteById(report_id);
    }

    @PutMapping({"/{report_id}"})
    public void createNewReport(@PathVariable Long report_id, @RequestBody ReportQuery reportQuery){

        reportService.createReport(report_id,reportQuery);

    }
}
