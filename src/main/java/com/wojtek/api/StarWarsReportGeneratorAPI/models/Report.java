package com.wojtek.api.StarWarsReportGeneratorAPI.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Report {

    @Id
    private Long report_id;

    @Column(name = "query_criteria_character_phrase")
    private String query_criteria_character_phrase;

    @Column(name = "query_criteria_planet_name")
    private String query_criteria_planet_name;

    @Column(name = "film_id")
    private Long film_id;

    @Column(name = "film_name")
    private String film_name;

    @Column(name = "character_id")
    private Long character_id;

    @Column(name = "character_name")
    private String character_name;

    @Column(name = "planet_id")
    private Long planet_id;

    @Column(name = "planter_name")
    private String planet_name;

}
