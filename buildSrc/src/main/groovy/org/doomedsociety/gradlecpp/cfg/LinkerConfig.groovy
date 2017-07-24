package org.doomedsociety.gradlecpp.cfg

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.gradle.api.Project

@CompileStatic @TypeChecked
abstract class LinkerConfig extends ToolConfig {
    List<String> libDirectories = []
    List<String> extraLibs = []

    void projectLibpath(Project p, String... dirs) {
        dirs.each { String d ->
            libDirectories.add(p.projectDir.absolutePath + d)
        }
    }

    void libpath(String... dirs) {
        libDirectories.addAll dirs
    }

    void extraLibs(String... libs) {
        extraLibs.addAll libs
    }
}
