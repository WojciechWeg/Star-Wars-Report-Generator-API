package com.wojtek.api.StarWarsReportGeneratorAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ThereIsNoFilmsForGivenCharacterException extends  RuntimeException{

    public ThereIsNoFilmsForGivenCharacterException(String s) {
        super(s);
    }
}
