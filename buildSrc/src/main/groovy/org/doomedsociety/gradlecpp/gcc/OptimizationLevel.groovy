package org.doomedsociety.gradlecpp.gcc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.ParameterOption

@CompileStatic @TypeChecked
enum OptimizationLevel implements ParameterOption {

    DISABLE('-O0'),
    LEVEL_1('-O1'),
    LEVEL_2('-O2'),
    LEVEL_3('-O3'),
    OPTIMIZE_SIZE('-Os'),
    OPTIMIZE_SPEED('-Ofast'),
    OPTIMIZE_DEBUG('-Og')

    String param;

    OptimizationLevel(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}
