package org.doomedsociety.gradlecpp.cfg

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.gcc.GccToolchainConfig
import org.doomedsociety.gradlecpp.gcc.GccToolchainConfigurator
import org.doomedsociety.gradlecpp.msvc.MsvcToolchainConfig
import org.doomedsociety.gradlecpp.msvc.MsvcToolchainConfigurator

@CompileStatic @TypeChecked
class ToolchainConfiguratorFactory {
    static BaseConfigurator createConfigurator(ToolchainConfig cfg) {
        switch (cfg.class) {
            case MsvcToolchainConfig: return new MsvcToolchainConfigurator()
            case GccToolchainConfig: return new GccToolchainConfigurator()
            default: throw new RuntimeException("Unknown ToolchainConfig ${cfg.class.name}")
        }
    }
}
