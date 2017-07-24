package org.doomedsociety.gradlecpp.gcc

import org.doomedsociety.gradlecpp.GradleCppUtils
import org.doomedsociety.gradlecpp.cfg.BaseConfigurator
import org.doomedsociety.gradlecpp.cfg.ToolchainConfig
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.language.PreprocessingTool
import org.gradle.language.nativeplatform.tasks.AbstractNativeCompileTask
import org.gradle.nativeplatform.NativeBinarySpec
import org.gradle.nativeplatform.Tool

class GccToolchainConfigurator extends BaseConfigurator {

    static void setupPrecompiledHeaders(Project p, NativeBinarySpec bin, GccToolchainConfig.PrecompilerHeaderOptions pchConfig) {
        def pchDir = new File(p.buildDir, "pch")
        pchDir = new File(pchDir, "${bin.name}_${bin.targetPlatform.name}_${bin.buildType.name}_${bin.flavor.name}")

        GradleCppUtils.onTasksCreated(p, GccToolchainConfigurator.name + '.setupPrecompiledHeaders()', {
            GradleCppUtils.getCompileTasks(bin).each { Task compileTask ->
                compileTask.doFirst {
                    pchDir.mkdirs()
                }

                compileTask.compilerArgs = new ArrayList(compileTask.compilerArgs)

                compileTask.compilerArgs << '-pch-dir'
                compileTask.compilerArgs << pchDir.absolutePath
                compileTask.compilerArgs << '-pch'
            }
        })
    }

    static void applyCompilerConfig(Project p, NativeBinarySpec bin, Tool compiler, GccToolchainConfig.CompilerOptions cfg, Set<String> ignoreList) {
        cfg.includeDirs.each { incDir ->
            compiler.args('-I' + incDir)
        }

        cfg.extraDefines.each { kv ->
            if (kv.value == null) {
                compiler.args("-D${kv.key}")
            } else {
                compiler.args("-D${kv.key}=${kv.value}")
            }
        }

        applyToolConfig(compiler, cfg, ignoreList)

        if (cfg.pchConfig?.enabled) {
            setupPrecompiledHeaders(p, bin, cfg.pchConfig)
        }

        if (cfg.positionIndependentCode != null) {
            GradleCppUtils.onTasksCreated(p, GccToolchainConfigurator.name + '.setupPrecompiledHeaders()', {
                GradleCppUtils.getCompileTasks(bin).each { Task t ->
                    if (t instanceof AbstractNativeCompileTask) {
                        t.setPositionIndependentCode(cfg.positionIndependentCode)
                    }

                }
            })
        }
    }

    static void applyLinkerConfig(Project p, NativeBinarySpec bin, Tool linker, GccToolchainConfig.LinkerOptions cfg) {
        applyToolConfig(linker, cfg, null)
        cfg.libDirectories.each { incDir ->
            linker.args('-L' + incDir)
        }

        cfg.extraLibs.each { lib ->
            linker.args('-l' + lib)
        }
    }

    @Override
    void applyToolchainConfig(Project p, ToolchainConfig cfg, NativeBinarySpec bin) {
        GccToolchainConfig config = (GccToolchainConfig) cfg

        boolean isCpp
        PreprocessingTool compiler

        if (null != bin.convention.findByName('cCompiler')) {
            compiler = (PreprocessingTool) bin.cCompiler
            isCpp = false
        } else if (null != bin.convention.findByName('cppCompiler')) {
            compiler = (PreprocessingTool) bin.cppCompiler
            isCpp = true
        } else {
            throw new RuntimeException("Binary ${bin} has no attached C/C++ compiler")
        }

        applyCompilerConfig(p, bin, compiler, config.compilerOptions, null)
        applyLinkerConfig(p, bin, bin.linker, config.linkerOptions)
        applyToolConfig(bin.staticLibArchiver, config.librarianOptions, null)
    }


}
