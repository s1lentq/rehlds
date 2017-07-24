package org.doomedsociety.gradlecpp.cfg

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.gradle.api.Project
import org.gradle.nativeplatform.NativeBinarySpec
import org.gradle.nativeplatform.Tool

import java.lang.reflect.Field

@CompileStatic @TypeChecked
abstract class BaseConfigurator {

    abstract void applyToolchainConfig(Project p, ToolchainConfig cfg, NativeBinarySpec bin);

    static void applyToolConfig(Tool tool, ToolConfig cfgModel, Set<String> ignoreList) {
        cfgModel.class.declaredFields.each { Field f ->
            String param = null

            if (f.name in ignoreList) {
                return
            }

            if (ParameterOption.isAssignableFrom(f.type)) {
                f.setAccessible(true)
                ParameterOption po = (ParameterOption) f.get(cfgModel)
                param = po?.parameter
            } else {
                BoolParam boolParam = null
                StringParam stringParam = null

                f.declaredAnnotations.each { a ->
                    switch (a.class) {
                        case BoolParam: boolParam = (BoolParam) a; break;
                        case StringParam: stringParam = (StringParam) a; break;
                    }
                }

                if (boolParam) {
                    f.setAccessible(true)
                    Boolean val = (Boolean) f.get(cfgModel)
                    if (val != null) {
                        param = val ? boolParam.on() : boolParam.off()
                    }
                } else if (stringParam) {
                    f.setAccessible(true)
                    String prefix = stringParam.prefix()
                    Object val = f.get(cfgModel)
                    if (val != null) {
                        param = (prefix ?: '') + val
                    }
                }
            }

            if (param) {
                tool.args(param)
            }
        }

        cfgModel.extraArgs?.each { a ->
            tool.args(a)
        }
    }
}
