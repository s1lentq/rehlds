package org.doomedsociety.gradlecpp.gcc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.BoolParam
import org.doomedsociety.gradlecpp.cfg.CompilerConfig
import org.doomedsociety.gradlecpp.cfg.LinkerConfig
import org.doomedsociety.gradlecpp.cfg.StringParam
import org.doomedsociety.gradlecpp.cfg.ToolConfig
import org.doomedsociety.gradlecpp.cfg.ToolchainConfig

@CompileStatic @TypeChecked
class GccToolchainConfig extends ToolchainConfig {

    static class PrecompilerHeaderOptions {
        boolean enabled
        String pchSourceSet
    }

    static class CompilerOptions extends CompilerConfig {
        @BoolParam(on = '-fno-builtin', off = '') Boolean noBuiltIn
        @BoolParam(on = '-intel-extensions', off = '-no-intel-extensions') Boolean intelExtensions
        @BoolParam(on = '-fasm-blocks', off = '-fno-asm-blocks') Boolean asmBlocks

        @BoolParam(on = '-ipo', off = '') Boolean interProceduralOptimizations
        @BoolParam(on = '-fstack-protector', off = '-fno-stack-protector') Boolean stackProtector

        OptimizationLevel optimizationLevel

        @StringParam(prefix = '-std=') String languageStandard

        PrecompilerHeaderOptions pchConfig
        Boolean positionIndependentCode
    }

    static class LinkerOptions extends LinkerConfig {
        @BoolParam(on = '-ipo', off = '') Boolean interProceduralOptimizations
        @BoolParam(on = '-s', off = '') Boolean stripSymbolTable
        @BoolParam(on = '-static-libgcc', off = '') Boolean staticLibGcc
        @BoolParam(on = '-static-intel', off = '') Boolean staticIntel
        @BoolParam(on = '-static-libstdc++', off = '') Boolean staticLibStdCpp
    }

    static class LibrarianOptions extends ToolConfig {

    }

    CompilerOptions compilerOptions
    LinkerOptions linkerOptions
    LibrarianOptions librarianOptions

    @Override
    CompilerConfig getCompilerConfig() {
        return compilerOptions
    }

    @Override
    LinkerConfig getLinkerConfig() {
        return linkerOptions
    }
}
