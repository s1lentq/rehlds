package org.doomedsociety.gradlecpp.gcc

import org.gradle.api.Transformer;
import org.gradle.internal.operations.BuildOperationProcessor;
import org.gradle.nativeplatform.toolchain.internal.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GccCompatibleNativeCompiler<T extends NativeCompileSpec> extends NativeCompiler<T> {

    GccCompatibleNativeCompiler(BuildOperationProcessor buildOperationProcessor, CommandLineToolInvocationWorker commandLineTool, CommandLineToolContext invocationContext, final ArgsTransformer<T> argsTransformer, Transformer<T, T> specTransformer, String objectFileExtension, boolean useCommandFile) {
        super(buildOperationProcessor, commandLineTool, invocationContext, argsTransformer, specTransformer, objectFileExtension, useCommandFile);
    }

    @Override
    protected List<String> getOutputArgs(T spec, File outputFile) {
        return Arrays.asList("-o", outputFile.getAbsolutePath());
    }

    @Override
    protected void addOptionsFileArgs(List<String> args, File tempDir) {
        OptionsFileArgsWriter writer = new GccOptionsFileArgsWriter(tempDir);
        // modifies args in place
        writer.execute(args);
    }

    @Override
    protected List<String> getPCHArgs(T spec) {
        List<String> pchArgs = new ArrayList<String>();
        if (spec.getPrefixHeaderFile() != null) {
            pchArgs.add("-include");
            pchArgs.add(spec.getPrefixHeaderFile().getAbsolutePath());
        }
        return pchArgs;
    }
}
