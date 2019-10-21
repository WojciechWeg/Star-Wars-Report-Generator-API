package com.wojtek.api.StarWarsReportGeneratorAPI.services;

import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.ReportQuery;
import com.wojtek.api.StarWarsReportGeneratorAPI.reposotories.ReportRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void createReport(Long report_id, ReportQuery reportQuery) {

            Report report = Report.builder()
                    .report_id(report_id)
                    .query_criteria_character_phrase(reportQuery.getQuery_criteria_character_phrase())
                    .query_criteria_planet_name(reportQuery.getQuery_criteria_planet_name())
                    .build();

            reportRepository.save(report);

    }
}
