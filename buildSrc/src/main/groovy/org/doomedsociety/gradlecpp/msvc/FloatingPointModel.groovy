package org.doomedsociety.gradlecpp.msvc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.ParameterOption

@CompileStatic @TypeChecked
public enum FloatingPointModel implements ParameterOption {
    PRECISE('/fp:precise'),
    STRICT('/fp:strict'),
    FAST('/fp:fast')

    String param;

    FloatingPointModel(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}

