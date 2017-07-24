package org.doomedsociety.gradlecpp.msvc

import org.doomedsociety.gradlecpp.cfg.ParameterOption

public enum EnhancedInstructionsSet implements ParameterOption {
    DISABLED('/arch:IA32'),
    SSE('/arch:SSE'),
    SSE2('/arch:SSE2'),
    AVX('/arch:AVX'),
    AVX2('/arch:AVX2')

    String param;

    EnhancedInstructionsSet(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}