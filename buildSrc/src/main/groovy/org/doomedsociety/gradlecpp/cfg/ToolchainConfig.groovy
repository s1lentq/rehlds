package org.doomedsociety.gradlecpp.cfg

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.gradle.api.Project

@CompileStatic @TypeChecked
public abstract class ToolchainConfig {
    abstract CompilerConfig getCompilerConfig()
    abstract LinkerConfig getLinkerConfig()

    void projectInclude(Project p, String... dirs) {
        compilerConfig.projectInclude(p, dirs)
    }

    void includeDirs(String... dirs) {
        compilerConfig.include(dirs)
    }

    void singleDefines(String... defs) {
        compilerConfig.singleDefines(defs)
    }

    void defines(Map defs) {
        compilerConfig.defines(defs)
    }

    void projectLibpath(Project p, String... dirs) {
        linkerConfig.projectLibpath(p, dirs)
    }

    void libpath(String... dirs) {
        linkerConfig.libpath(dirs)
    }

    void extraLibs(String... libs) {
        linkerConfig.extraLibs(libs)
    }
}
