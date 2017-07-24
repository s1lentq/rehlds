package org.doomedsociety.gradlecpp.msvc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.ParameterOption

@CompileStatic @TypeChecked
public enum CallingConvention implements ParameterOption {

    CDECL('/Gd'),
    FASTCALL('/Gr'),
    STDCALL('/Gz'),
    VECTORCALL('/Gv')

    String param;

    CallingConvention(String param) {
        this.param = param
    }

    @Override
    String getParameter() {
        return param
    }
}