package com.wojtek.api.StarWarsReportGeneratorAPI.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.ReportQuery;
import com.wojtek.api.StarWarsReportGeneratorAPI.reposotories.ReportRepository;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final APIService apiService;
    private final Map<String,String> planetNamePlanetLinkMap;

    public ReportService(ReportRepository reportRepository, APIService apiService) {
        this.reportRepository = reportRepository;
        this.apiService = apiService;
        this.planetNamePlanetLinkMap = new HashMap<>();
    }

    public void createReport(Long report_id, ReportQuery reportQuery) {

            Report report = Report.builder()
                    .report_id(report_id)
                    .query_criteria_character_phrase(reportQuery.getQuery_criteria_character_phrase())
                    .query_criteria_planet_name(reportQuery.getQuery_criteria_planet_name())
                    .build();

            reportRepository.save(report);

    }

    public void startPhase() throws Exception {

            JsonObject jsonObject = apiService.getBuilder("planets");
            String next  = jsonObject.get("next").toString();
            char pageNumber = next.toString().charAt(next.length()-2);

            while(!next.equals("null")) {
                JsonArray planetArray = jsonObject.getAsJsonArray("results");

                collectPlanetInfo(planetArray);

                jsonObject = apiService.getBuilder("planets/?page="+pageNumber);
                next = jsonObject.get("next").toString();
                pageNumber = next.toString().charAt(next.length()-2);

                if(next.equals("null")){
                    planetArray = jsonObject.getAsJsonArray("results");

                    collectPlanetInfo(planetArray);

                    break;
                }

            }


    }

    private void collectPlanetInfo(JsonArray planetArray) {
        for (int i = 0; i < planetArray.size(); i++) {
            JsonObject planetJson = planetArray.get(i).getAsJsonObject();
            String planetName = planetJson.get("name").toString();
            String planetUrl = planetJson.get("url").toString();
            planetNamePlanetLinkMap.put(planetName, planetUrl);
        }

    }
}
