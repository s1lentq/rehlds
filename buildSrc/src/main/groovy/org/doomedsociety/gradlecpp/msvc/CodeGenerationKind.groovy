package org.doomedsociety.gradlecpp.msvc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.ParameterOption

@CompileStatic @TypeChecked
public enum CodeGenerationKind implements ParameterOption {
    MULTITHREADED('/MT'),
    MULTITHREADED_DLL('/MD'),
    MULTITHREADED_DEBUG('/MTd'),
    MULTITHREADED_DEBUG_DLL('/MDd')

    String param;

    CodeGenerationKind(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}