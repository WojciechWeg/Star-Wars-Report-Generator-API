package com.wojtek.api.StarWarsReportGeneratorAPI.services;

import com.wojtek.api.StarWarsReportGeneratorAPI.exceptions.NotFoundException;
import com.wojtek.api.StarWarsReportGeneratorAPI.exceptions.PlanetHasNoResidentsExceptions;
import com.wojtek.api.StarWarsReportGeneratorAPI.exceptions.ThereIsNoSuchResidentException;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.ReportQuery;
import com.wojtek.api.StarWarsReportGeneratorAPI.reposotories.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    private ReportService reportService;

    @Mock
    private ReportRepository reportRepository;

    private Report report1,report2, report3;
    private List<Report> reportList;
    private ReportQuery startQuery;

    @BeforeEach
    void init() throws Exception {

        reportService = new ReportService(reportRepository);

        reportList = new ArrayList<>();

        report1 =  Report.builder()
                .report_id(1L)
                .query_criteria_character_phrase("Luke Skywalker")
                .query_criteria_planet_name("Tatooine")
                .film_id(2L)
                .film_name("The Empire Strikes Back")
                .character_id(1L)
                .character_name("Luke Skywalker")
                .planet_id(1L)
                .planet_name("Tatooine").build();

        report2 =  Report.builder()
                .report_id(2L)
                .query_criteria_character_phrase("bot")
                .query_criteria_planet_name("Bespin")
                .film_id(2L)
                .film_name("The Empire Strikes Back")
                .character_id(26L)
                .character_name("Lobot")
                .planet_id(6L)
                .planet_name("Bespin").build();

        report3 =  Report.builder()
                .report_id(3L)
                .query_criteria_character_phrase("R2-D2")
                .query_criteria_planet_name("Naboo")
                .film_id(2L)
                .film_name("The Empire Strikes Back")
                .character_id(3L)
                .character_name("R2-D2")
                .planet_id(8L)
                .planet_name("Naboo").build();

        reportList.add(report1);
        reportList.add(report2);

        startQuery = new ReportQuery("Shmi Skywalker","Tatooine");
        reportService.startPhase();
    }

    @Test
    void createReport() throws Exception {

        Report returnedReport = reportService.createReport(3L,startQuery);
        
        assertEquals(Long.valueOf(3L), returnedReport.getReport_id());
        assertEquals("Tatooine", returnedReport.getPlanet_name());
        assertEquals("Shmi Skywalker", returnedReport.getCharacter_name());
        assertEquals(Long.valueOf(43),returnedReport.getCharacter_id());
        assertEquals(Long.valueOf(5),returnedReport.getFilm_id());
        assertEquals("Attack of the Clones",returnedReport.getFilm_name());
        assertEquals(Long.valueOf(1),returnedReport.getPlanet_id());
        assertEquals("Shmi Skywalker",returnedReport.getQuery_criteria_character_phrase());
        assertEquals("Tatooine",returnedReport.getQuery_criteria_planet_name());

    }

    @Test
    void createReportThrowPlanetNotFound(){
        startQuery.setQuery_criteria_planet_name("abc");
        assertThrows(NotFoundException.class, () -> reportService.createReport(4L,startQuery));

    }

    @Test
    void createReportThrowPlanetHasNoResidents(){
        startQuery.setQuery_criteria_planet_name("Dagobah");
        assertThrows(PlanetHasNoResidentsExceptions.class, () -> reportService.createReport(4L,startQuery));
    }

    @Test
    void createReportThrowNoSuchResident(){
        startQuery.setQuery_criteria_character_phrase("abcd");
        assertThrows(ThereIsNoSuchResidentException.class, () -> reportService.createReport(4L,startQuery));

    }

}