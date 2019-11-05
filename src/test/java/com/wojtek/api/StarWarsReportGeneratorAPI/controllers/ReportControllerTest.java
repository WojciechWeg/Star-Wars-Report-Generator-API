package com.wojtek.api.StarWarsReportGeneratorAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wojtek.api.StarWarsReportGeneratorAPI.exceptions.NotFoundException;
import com.wojtek.api.StarWarsReportGeneratorAPI.exceptions.ThereIsNoFilmsForGivenCharacterException;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.ReportQuery;
import com.wojtek.api.StarWarsReportGeneratorAPI.reposotories.ReportRepository;
import com.wojtek.api.StarWarsReportGeneratorAPI.services.ReportService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private ReportRepository reportRepository;

    Report report1,report2, report3;
    List<Report> reportList;
    ReportQuery startQuery;

    @BeforeEach
    void init(){

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

         startQuery = new ReportQuery("R2-D2","Naboo");

    }


    @Test
    void getListOfReports() throws Exception {

        when(reportRepository.findAll()).thenReturn(reportList);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/report/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$.[0].report_id",Matchers.is(1)))
                .andExpect(jsonPath("$.[1].report_id",Matchers.is(2)));

    }

    @Test
    void getReport() throws Exception {

        when(reportRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(report1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/report/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.report_id",Matchers.is(1)));

    }

    @Test
    void getReportInvalidArgument() throws Exception {

        when(reportRepository.findById(any())).thenThrow(new NumberFormatException(""));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/report/34era"))
                .andExpect(status().isBadRequest())
                .andReturn();


    }

    @Test
    void getReportThrowNotFound() throws Exception {

        when(reportRepository.findById(any())).thenThrow(new NotFoundException("No such element."));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/report/4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("No such element.")))
                .andReturn();

    }

    @Test
    void deleteAllReports() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/report/"))
                .andExpect(status().isOk())
                .andReturn();

        verify(reportRepository,times(1)).deleteAll();

    }

    @Test
    void deleteReportById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/report/1"))
                .andExpect(status().isOk())
                .andReturn();

        verify(reportRepository,times(1)).deleteById(1L);

    }

    @Test
    void deleteReportByIdThrowInvalidArgument() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/report/1fs"))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    void createNewReport() throws Exception {

        when(reportService.createReport(any(),any())).thenReturn(report3);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/report/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(startQuery)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist())
                .andReturn();

    }

    @Test
    void createNewReportThrowPlanetNotFound() throws Exception {

        when(reportService.createReport(any(),any())).thenThrow( new NotFoundException("No such planet."));

        startQuery.setQuery_criteria_planet_name("abc");

        mockMvc.perform(MockMvcRequestBuilders
                .put("/report/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(startQuery)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",Matchers.is("No such planet.")))
                .andReturn();
    }

    @Test
    void createNewReportThrowPlanetHasNoResidents() throws Exception {

        when(reportService.createReport(any(),any())).thenThrow( new NotFoundException("Planet has no residents."));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/report/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(startQuery)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",Matchers.is("Planet has no residents.")))
                .andReturn();

    }

    @Test
    void createNewReportThrowNoSuchResident() throws Exception {

        when(reportService.createReport(any(),any())).thenThrow( new NotFoundException("Planet has no residents."));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/report/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(startQuery)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",Matchers.is("Planet has no residents.")))
                .andReturn();

    }

    @Test
    void creteNewReportThrowNoFilmsForGivenCharacter() throws Exception {

        when(reportService.createReport(any(),any()))
                .thenThrow( new ThereIsNoFilmsForGivenCharacterException("There is no films for given character."));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/report/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(startQuery)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",Matchers.is("There is no films for given character.")))
                .andReturn();
    }

    @Test
    void createNewReportThrowInvalidArgument() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .put("/report/1fs"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}