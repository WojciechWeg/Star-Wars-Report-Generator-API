package com.wojtek.api.StarWarsReportGeneratorAPI.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="reports")
@Data
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_id;

    @Column(name = "query_criteria_character_phrase")
    String query_criteria_character_phrase;

    @Column(name = "query_criteria_planet_name")
    String query_criteria_planet_name;

    @Column(name = "film_id")
    Long film_id;

    @Column(name = "film_name")
    String film_name;

    @Column(name = "character_id")
    Long character_id;

    @Column(name = "character_name")
    String character_name;

    @Column(name = "planet_id")
    Long planet_id;

    @Column(name = "planter_name")
    String planet_name;

}
