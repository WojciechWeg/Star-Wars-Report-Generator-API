package com.wojtek.api.StarWarsReportGeneratorAPI.bootstrap;


import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import com.wojtek.api.StarWarsReportGeneratorAPI.reposotories.ReportRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final ReportRepository reportRepository;

    public Bootstrap(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        loadReports();

    }

    private void loadReports(){

        Report report1 =  Report.builder()
                .report_id(1L)
                .query_criteria_character_phrase("ABC")
                .query_criteria_planet_name("XYZ")
                .film_id(2L)
                .film_name("123")
                .character_id(3L)
                .character_name("Kowalski")
                .planet_id(4L)
                .planet_name("QWE").build();

        Report report2 =  Report.builder()
                .report_id(2L)
                .query_criteria_character_phrase("ABC")
                .query_criteria_planet_name("XYZ")
                .film_id(2L)
                .film_name("123")
                .character_id(3L)
                .character_name("ABACKI")
                .planet_id(4L)
                .planet_name("QWE").build();

        reportRepository.save(report1);
        reportRepository.save(report2);




    }
}
