package com.wojtek.api.StarWarsReportGeneratorAPI.bootstrap;


import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import com.wojtek.api.StarWarsReportGeneratorAPI.reposotories.ReportRepository;
import com.wojtek.api.StarWarsReportGeneratorAPI.services.ReportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final ReportRepository reportRepository;
    private final ReportService reportService;

    public Bootstrap(ReportRepository reportRepository, ReportService reportService) {
        this.reportRepository = reportRepository;
        this.reportService = reportService;
    }

    @Override
    public void run(String... args) throws Exception {

        reportService.startPhase();

        loadReports();

    }

    private void loadReports() {

        Report report1 =  Report.builder()
                .report_id(1L)
                .query_criteria_character_phrase("Luke Skywalker")
                .query_criteria_planet_name("Tatooine")
                .film_id(2L)
                .film_name("The Empire Strikes Back")
                .character_id(1L)
                .character_name("Luke Skywalker")
                .planet_id(1L)
                .planet_name("Tatooine").build();

        Report report2 =  Report.builder()
                .report_id(2L)
                .query_criteria_character_phrase("bot")
                .query_criteria_planet_name("Bespin")
                .film_id(2L)
                .film_name("The Empire Strikes Back")
                .character_id(26L)
                .character_name("Lobot")
                .planet_id(6L)
                .planet_name("Bespin").build();


        reportRepository.save(report1);
        reportRepository.save(report2);


    }
}
