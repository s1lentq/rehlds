package org.doomedsociety.gradlecpp.msvc

import org.doomedsociety.gradlecpp.GradleCppUtils
import org.doomedsociety.gradlecpp.cfg.BaseConfigurator
import org.doomedsociety.gradlecpp.cfg.ToolchainConfig
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.language.PreprocessingTool
import org.gradle.model.internal.core.DirectNodeModelAction
import org.gradle.model.internal.core.ModelActionRole
import org.gradle.model.internal.core.ModelPath
import org.gradle.model.internal.core.ModelReference
import org.gradle.model.internal.core.MutableModelNode
import org.gradle.model.internal.core.rule.describe.ModelRuleDescriptor
import org.gradle.model.internal.core.rule.describe.SimpleModelRuleDescriptor
import org.gradle.model.internal.registry.ModelRegistry
import org.gradle.nativeplatform.NativeBinarySpec
import org.gradle.nativeplatform.StaticLibraryBinarySpec
import org.gradle.nativeplatform.Tool

class MsvcToolchainConfigurator extends BaseConfigurator {

    static final Set<String> cppOnlyCompilerOpts = new HashSet(['cppExceptions'])

    static void applyCompilerConfig(Project p, NativeBinarySpec bin, Tool compiler, MsvcToolchainConfig.CompilerOptions cfg, Set<String> ignoreList) {
        cfg.includeDirs.each { incDir ->
            compiler.args('/I' + incDir)
        }

        cfg.extraDefines.each { kv ->
            if (kv.value == null) {
                compiler.args("/D${kv.key}")
            } else {
                compiler.args("/D${kv.key}=${kv.value}")
            }
        }

        applyToolConfig(compiler, cfg, ignoreList)

        if (cfg.pchConfig?.enabled) {
            setupPrecompiledHeaders(p, bin, cfg.pchConfig)
        }
    }

    static void applyLinkerConfig(Tool linker, MsvcToolchainConfig.LinkerOptions cfg) {
        applyToolConfig(linker, cfg, null)

        if (cfg.definitionFile) {
            linker.args('/DEF:' + cfg.definitionFile)
        }

        cfg.libDirectories.each { incDir ->
            linker.args('/LIBPATH:' + incDir)
        }

        cfg.extraLibs.each { lib ->
            linker.args(lib)
        }
    }

    static void setupPrecompiledHeaders(Project p, NativeBinarySpec bin, MsvcToolchainConfig.PrecompiledHeadersConfig pchConfig) {
        def pchDir = new File(p.buildDir, "pch")
        def pchFile = new File(pchDir, "${bin.name}_${bin.targetPlatform.name}_${bin.buildType.name}_${bin.flavor.name}.pch")

        GradleCppUtils.onTasksCreated(p, MsvcToolchainConfigurator.name + '.setupPrecompiledHeaders()', {
            def pchInfo = GradleMsvcUtils.getPchCompileTask(bin, pchConfig.pchSourceSet)
            if (!pchInfo) {
                throw new RuntimeException("Can't find PCH task for project ${p.name} / binary ${bin.name}")
            }

            pchInfo.pchTask.doFirst {
                pchDir.mkdirs()
            }

            pchInfo.pchTask.compilerArgs = new ArrayList(pchInfo.pchTask.compilerArgs)
            pchInfo.pchTask.compilerArgs << '/Yc' + pchConfig.pchHeader
            pchInfo.pchTask.compilerArgs << '/Fp' + pchFile.absolutePath
            pchInfo.compileTasks.each { compileTask ->
                compileTask.compilerArgs = new ArrayList(compileTask.compilerArgs)
                compileTask.compilerArgs << '/Yu' + pchConfig.pchHeader
                compileTask.compilerArgs << '/Fp' + pchFile.absolutePath
                compileTask.dependsOn pchInfo.pchTask
            }
        })
    }

    static void setupPdbGeneration(Project p, MsvcToolchainConfig config, NativeBinarySpec bin, Tool compiler, Tool linker) {
        if (!config.generatePdb) {
            return
        }

        if (!(bin instanceof StaticLibraryBinarySpec)) {
            def outputFile = GradleCppUtils.getBinaryOutputFile(bin)
            def linkerPdbFile = GradleCppUtils.changeFileExt(outputFile, '.pdb')
            linker.args "/PDB:${linkerPdbFile.absolutePath}"
        }

        def pdbDir = new File(p.buildDir, "pdb")
        def pdbFile = new File(pdbDir, "${bin.name}_${bin.targetPlatform.name}_${bin.buildType.name}_${bin.flavor.name}.pdb")
        compiler.args('/Fd' + pdbFile.absolutePath)
        //compiler.args('/FS')

        GradleCppUtils.onTasksCreated(p, MsvcToolchainConfigurator.name + '.setupPdbGeneration()', {
            def compileTasks = GradleCppUtils.getCompileTasks(bin)
            compileTasks.each { compileTask ->
                compileTask.doFirst {
                    pdbDir.mkdirs()
                }
            }
        })
    }


    @Override
    void applyToolchainConfig(Project p, ToolchainConfig cfg, NativeBinarySpec bin) {
        MsvcToolchainConfig config = (MsvcToolchainConfig) cfg
        PreprocessingTool compiler

        boolean isCpp

        if (null != bin.convention.findByName('cCompiler')) {
            compiler = (PreprocessingTool) bin.cCompiler
            isCpp = false
        } else if (null != bin.convention.findByName('cppCompiler')) {
            compiler = (PreprocessingTool) bin.cppCompiler
            isCpp = true
        } else {
            throw new RuntimeException("Binary ${bin} has no attached C/C++ compiler")
        }

        applyCompilerConfig(p, bin, compiler, config.compilerOptions, isCpp ? null : cppOnlyCompilerOpts)
        applyLinkerConfig(bin.linker, config.linkerOptions)
        applyToolConfig(bin.staticLibArchiver, config.librarianOptions, null)
        setupPdbGeneration(p, config, bin, compiler, bin.linker)


    }
}
