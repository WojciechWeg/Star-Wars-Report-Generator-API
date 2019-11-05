package com.wojtek.api.StarWarsReportGeneratorAPI.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wojtek.api.StarWarsReportGeneratorAPI.exceptions.*;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import com.wojtek.api.StarWarsReportGeneratorAPI.models.ReportQuery;
import com.wojtek.api.StarWarsReportGeneratorAPI.reposotories.ReportRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final APIService apiService;
    private final Map<String,String> planetNamePlanetLinkMap;
    private boolean startPhaseIsOnGoing;

    public ReportService(ReportRepository reportRepository, APIService apiService) {
        this.reportRepository = reportRepository;
        this.apiService = apiService;
        this.planetNamePlanetLinkMap = new HashMap<>();
        this.startPhaseIsOnGoing = false;
    }

    public Report createReport(Long report_id, ReportQuery reportQuery) throws Exception {

            if(startPhaseIsOnGoing)
                throw new StartPhaseIsOnGoingException("Start phase is on going now. Please retry one more time in a minute.");

            System.out.println(planetNamePlanetLinkMap);

            Report report = Report.builder()
                    .report_id(report_id)
                    .query_criteria_character_phrase(reportQuery.getQuery_criteria_character_phrase())
                    .query_criteria_planet_name(reportQuery.getQuery_criteria_planet_name())
                    .build();

            if(!planetNamePlanetLinkMap.containsKey(reportQuery.getQuery_criteria_planet_name()))
                throw new NotFoundException("There is no such planet.");

            String planetUrl = planetNamePlanetLinkMap.get(reportQuery.getQuery_criteria_planet_name());
            Long planetId = retrieveIdFromLastSegment(planetUrl);

            report.setPlanet_id(planetId);
            report.setPlanet_name(reportQuery.getQuery_criteria_planet_name());

            JsonArray residentArray = apiService.getBuilderFullPath(planetUrl).getAsJsonArray("residents");

            if(residentArray.size()==0)
                throw new PlanetHasNoResidentsExceptions("This planet has no residents.");

            JsonObject residentObject=null;
            String residentName=null;
            Long characterId=null;
            boolean hasBeenFound = false;

            for(JsonElement residentUrl : residentArray){

                characterId = retrieveIdFromLastSegment(residentUrl.toString().replaceAll("\"",""));
                residentObject = apiService.getBuilderFullPath(residentUrl.toString().replaceAll("\"",""));

                residentName = residentObject.get("name").toString();

                if(residentName.contains(reportQuery.getQuery_criteria_character_phrase())){
                    hasBeenFound = true;
                    break;
                }
            }

            if (!hasBeenFound)
                throw new ThereIsNoSuchResidentException("There is no resident with given query.");

            report.setCharacter_id(characterId);
            report.setCharacter_name(residentName);

            JsonArray charactersFilms = residentObject.get("films").getAsJsonArray();

            if(charactersFilms.size() == 0)
                throw new ThereIsNoFilmsForGivenCharacterException("This character has no films.");


            Long filmId = retrieveIdFromLastSegment(charactersFilms.get(0).toString().replaceAll("\"",""));

            JsonObject filmObject =  apiService.getBuilderFullPath(charactersFilms.get(0).toString().replaceAll("\"",""));

            String filmName = filmObject.get("title").toString();

            report.setFilm_id(filmId);
            report.setFilm_name(filmName);


            reportRepository.save(report);

            return report;
    }

    private Long retrieveIdFromLastSegment(String planetUrl) throws URISyntaxException {
        URI uri = new URI(planetUrl);
        String[] segments = uri.getPath().split("/");
        String idStr = segments[segments.length-1];
        return Long.parseLong(idStr);
    }

    public void startPhase() throws Exception {

            startPhaseIsOnGoing = true;

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

            startPhaseIsOnGoing = false;

    }

    private void collectPlanetInfo(JsonArray planetArray) {
        for (int i = 0; i < planetArray.size(); i++) {
            JsonObject planetJson = planetArray.get(i).getAsJsonObject();
            String planetName = planetJson.get("name").toString().replaceAll("\"","");
            String planetUrl = planetJson.get("url").toString().replaceAll("\"","");
            planetNamePlanetLinkMap.put(planetName, planetUrl);
        }

    }

    public boolean isStartPhaseIsOnGoing() {
        return startPhaseIsOnGoing;
    }
}
