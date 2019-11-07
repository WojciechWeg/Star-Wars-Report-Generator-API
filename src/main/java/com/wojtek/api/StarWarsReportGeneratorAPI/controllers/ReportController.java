package com.wojtek.api.StarWarsReportGeneratorAPI.controllers;

import com.wojtek.api.StarWarsReportGeneratorAPI.exceptions.NotFoundException;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.ReportQuery;
import com.wojtek.api.StarWarsReportGeneratorAPI.reposotories.ReportRepository;
import com.wojtek.api.StarWarsReportGeneratorAPI.services.ReportService;
import org.springframework.dao.EmptyResultDataAccessException;
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
        return reportRepository.findAll();

    }

    @GetMapping({"/{report_id}"})
    public Report getReport(@PathVariable Long report_id) {
            return reportRepository.findById(report_id)
                    .orElseThrow(() -> new NotFoundException("No such report"));
    }

    @DeleteMapping
    public void deleteAllReports(){
         reportRepository.deleteAll();
    }

    @DeleteMapping({"/{report_id}"})
    public void deleteReportById(@PathVariable Long report_id){
        try {
            reportRepository.deleteById(report_id);
        }catch (EmptyResultDataAccessException e){
            throw new NotFoundException("No such report");
        }

    }

    @PutMapping({"/{report_id}"})
    public void createNewReport(@PathVariable Long report_id, @RequestBody ReportQuery reportQuery) throws Exception {
        reportService.createReport(report_id,reportQuery);
    }
}
