package org.doomedsociety.gradlecpp.msvc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.ParameterOption

@CompileStatic @TypeChecked
public enum LinkTimeCodeGenKind implements ParameterOption {
    DEFAULT(null),
    USE_LTCG('/LTCG'),
    LTCG_PROFILING_INSTRUMENT('/LTCG:PGInstrument'),
    LTCG_PROFILING_OPTIMIZE('/LTCG:PGOoptimize'),
    LTCG_PROFILING_UPDATE('/LTCG:PGUpdate')

    String param;

    LinkTimeCodeGenKind(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}