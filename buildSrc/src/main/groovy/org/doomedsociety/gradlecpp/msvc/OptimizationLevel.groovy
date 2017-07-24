package org.doomedsociety.gradlecpp.msvc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.ParameterOption

@CompileStatic @TypeChecked
public enum OptimizationLevel implements ParameterOption {
    DISABLED('/Od'),
    MINIMIZE_SIZE('/O1'),
    MAXIMIZE_SPEED('/O2'),
    FULL_OPTIMIZATION('/Ox')

    String param;

    OptimizationLevel(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}