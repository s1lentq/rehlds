package org.doomedsociety.gradlecpp.msvc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.ParameterOption

@CompileStatic @TypeChecked
public enum RuntimeChecks implements ParameterOption {
    DEFAULT(null),
    STACK_FRAMES('/RTCs'),
    UNINITIALIZED_VARS('/RTCu'),
    STACK_FRAMES_AND_UNINITIALIZED_VARS('/RTC1')

    String param;

    RuntimeChecks(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}
