package org.doomedsociety.gradlecpp.cfg

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.gradle.api.Project

@CompileStatic @TypeChecked
abstract class CompilerConfig extends ToolConfig {
    Map<String, String> extraDefines = [:]
    List<String> includeDirs = []

    void projectInclude(Project p, String... dirs) {
        dirs.each { String d ->
            includeDirs.add(p.projectDir.absolutePath + d)
        }
    }

    void include(String... dirs) {
        includeDirs.addAll dirs
    }

    void singleDefines(String... defs) {
        defs.each { String d ->
            extraDefines.put(d, null)
        }
    }

    void defines(Map defs) {
        defs.each { kv ->
            extraDefines[kv.key.toString()] = kv.value.toString()
        }
    }
}
