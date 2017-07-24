package org.doomedsociety.gradlecpp.cfg

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

@CompileStatic @TypeChecked
abstract class ToolConfig {
    List<String> extraArgs = []

    void args(String... args) {
        extraArgs.addAll args
    }
}
