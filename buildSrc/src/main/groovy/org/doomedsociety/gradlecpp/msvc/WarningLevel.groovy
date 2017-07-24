package org.doomedsociety.gradlecpp.msvc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.ParameterOption

@CompileStatic @TypeChecked
enum WarningLevel implements ParameterOption {
    DISABLE_ALL('/W0'),
    LEVEL_1('/W1'),
    LEVEL_2('/W2'),
    LEVEL_3('/W3'),
    LEVEL_4('/W4'),
    ENABLE_ALL('/Wall')

    String param;

    WarningLevel(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}
