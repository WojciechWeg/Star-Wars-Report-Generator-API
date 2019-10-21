package com.wojtek.api.StarWarsReportGeneratorAPI.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReportQuery {

    private String query_criteria_character_phrase;
    private String query_criteria_planet_name;



}
