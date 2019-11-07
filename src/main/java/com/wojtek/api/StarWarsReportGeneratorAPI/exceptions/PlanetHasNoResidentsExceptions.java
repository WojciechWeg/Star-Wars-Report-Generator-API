package com.wojtek.api.StarWarsReportGeneratorAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlanetHasNoResidentsExceptions extends RuntimeException{

    public PlanetHasNoResidentsExceptions(String s) {
        super(s);
    }
}
