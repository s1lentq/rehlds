package org.doomedsociety.gradlecpp.msvc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.doomedsociety.gradlecpp.cfg.BoolParam
import org.doomedsociety.gradlecpp.cfg.CompilerConfig
import org.doomedsociety.gradlecpp.cfg.LinkerConfig
import org.doomedsociety.gradlecpp.cfg.StringParam
import org.doomedsociety.gradlecpp.cfg.ToolConfig
import org.doomedsociety.gradlecpp.cfg.ToolchainConfig

@CompileStatic @TypeChecked
class MsvcToolchainConfig extends ToolchainConfig {

    static class PrecompiledHeadersConfig {
        boolean enabled
        String pchHeader
        String pchSourceSet
    }

    static class CompilerOptions extends CompilerConfig {
        CodeGenerationKind codeGeneration
        OptimizationLevel optimizationLevel
        DebugInfoFormat debugInfoFormat
        RuntimeChecks runtimeChecks
        CppExceptions cppExceptions
        WarningLevel warningLevel
        CallingConvention callingConvention
        EnhancedInstructionsSet enhancedInstructionsSet
        FloatingPointModel floatingPointModel


        @BoolParam(on = '/Gm', off = '/Gm-') Boolean enableMinimalRebuild
        @BoolParam(on = '/Oy', off = '/Oy-') Boolean omitFramePointers
        @BoolParam(on = '/GL', off = '') Boolean wholeProgramOptimization
        @BoolParam(on = '/Gy', off = '/Gy-') Boolean enabledFunctionLevelLinking
        @BoolParam(on = '/GS', off = '/GS-') Boolean enableSecurityCheck
        @BoolParam(on = '/analyze', off = '/analyze-') Boolean analyzeCode
        @BoolParam(on = '/sdl', off = '/sdl-') Boolean sdlChecks
        @BoolParam(on = '/WX', off = '/WX-') Boolean treatWarningsAsErrors
        @BoolParam(on = '/Zc:wchar_t', off = '/Zc:wchar_t-') Boolean treatWchartAsBuiltin
        @BoolParam(on = '/Zc:forScope', off = '/Zc:forScope-') Boolean forceConformanceInForLoopScope

        PrecompiledHeadersConfig pchConfig
    }

    static class LinkerOptions extends LinkerConfig {
        LinkTimeCodeGenKind linkTimeCodeGenKind
        ErrorReporting errorReportingMode

        @BoolParam(on = '/INCREMENTAL', off = '/INCREMENTAL:NO') Boolean enableIncrementalLinking
        @BoolParam(on = '/OPT:REF', off = '/OPT:NOREF') Boolean eliminateUnusedRefs
        @BoolParam(on = '/OPT:ICF', off = '/OPT:NOICF') Boolean enableCOMDATFolding
        @BoolParam(on = '/DEBUG', off = '') Boolean generateDebugInfo
        @BoolParam(on = '/NXCOMPAT', off = '/NXCOMPAT:NO') Boolean dataExecutionPrevention
        @BoolParam(on = '/DYNAMICBASE', off = '/DYNAMICBASE:NO') Boolean randomizedBaseAddress
        @BoolParam(on = '/FIXED', off = '/FIXED:NO') Boolean fixedBaseAddress
        @StringParam(prefix = '/BASE:') String baseAddress

        String definitionFile
    }

    static class LibrarianOptions extends ToolConfig {
        LinkTimeCodeGenKind linkTimeCodeGenKind
    }

    Boolean generatePdb

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
