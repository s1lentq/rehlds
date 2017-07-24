package org.doomedsociety.gradlecpp.cfg

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.gradle.api.Project
import org.gradle.nativeplatform.NativeBinarySpec

@CompileStatic @TypeChecked
class ToolchainConfigUtils {
    static void apply(Project p, ToolchainConfig cfg, NativeBinarySpec bin) {
        def configurator = ToolchainConfiguratorFactory.createConfigurator(cfg)
        configurator.applyToolchainConfig(p, cfg, bin)
    }
}
