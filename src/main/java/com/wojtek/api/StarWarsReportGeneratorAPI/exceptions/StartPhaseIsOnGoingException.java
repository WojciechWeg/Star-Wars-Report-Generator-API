package com.wojtek.api.StarWarsReportGeneratorAPI.exceptions;

public class StartPhaseIsOnGoingException extends  RuntimeException{

    public StartPhaseIsOnGoingException(String s) {
        super(s);
    }
}
