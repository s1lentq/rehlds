package org.doomedsociety.gradlecpp.msvc

import org.doomedsociety.gradlecpp.cfg.ParameterOption

public enum CppExceptions implements ParameterOption {
    DISABLED(null),
    ENABLED('/EHsc'),
    ENABLED_WITH_SEH('/EHa'),
    ENABLED_WITH_EXTERN_C_FUNCS('/EHs')

    String param;

    CppExceptions(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}