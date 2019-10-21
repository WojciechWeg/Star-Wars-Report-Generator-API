package com.wojtek.api.StarWarsReportGeneratorAPI.controllers;

import com.wojtek.api.StarWarsReportGeneratorAPI.services.ReportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ReportController.BASE_URL)
public class ReportController {

    public static final String BASE_URL = "report";

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public void getListOfReports(){}

    @GetMapping({"/report_id"})
    public void getReport(@PathVariable Long report_id){}

    @DeleteMapping
    public void deleteAllReports(){}

    @DeleteMapping({"/report_id"})
    public void deleteReportById(@PathVariable Long report_id){}

    @PutMapping({"/report_id"})
    public void createNewReport(@PathVariable Long report_id){}
}
