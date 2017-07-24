package org.doomedsociety.gradlecpp.msvc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.ParameterOption

@CompileStatic @TypeChecked
public enum ErrorReporting implements ParameterOption {
    PROMPT_IMMEDIATELY('/ERRORREPORT:PROMPT'),
    QUEUE_FOR_NEXT_LOGIN('/ERRORREPORT:QUEUE'),
    SEND_ERROR_REPORT('/ERRORREPORT:SEND'),
    NO_ERROR_REPORT('/ERRORREPORT:NONE')

    String param;

    ErrorReporting(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}
