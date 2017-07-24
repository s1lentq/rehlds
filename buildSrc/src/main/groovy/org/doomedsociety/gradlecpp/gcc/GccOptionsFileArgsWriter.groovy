package org.doomedsociety.gradlecpp.gcc

import com.google.common.collect.Lists;
import org.gradle.platform.base.internal.toolchain.ArgWriter;
import org.gradle.nativeplatform.toolchain.internal.OptionsFileArgsWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Uses an option file for arguments passed to GCC if possible.
 * Certain GCC options do not function correctly when included in an option file, so include these directly on the command line as well.
 */
class GccOptionsFileArgsWriter extends OptionsFileArgsWriter {
    private static final List<String> CLI_ONLY_ARGS = Arrays.asList("-m32", "-m64");

    public GccOptionsFileArgsWriter(File tempDir) {
        super(ArgWriter.unixStyleFactory(), tempDir);
    }

    @Override
    public List<String> transformArgs(List<String> originalArgs, File tempDir) {
        List<String> commandLineOnlyArgs = getCommandLineOnlyArgs(originalArgs);
        List<String> finalArgs = Lists.newArrayList();
        finalArgs.addAll(super.transformArgs(originalArgs, tempDir));
        finalArgs.addAll(commandLineOnlyArgs);
        return finalArgs;
    }

    private List<String> getCommandLineOnlyArgs(List<String> allArgs) {
        List<String> commandLineOnlyArgs = new ArrayList<String>(allArgs);
        commandLineOnlyArgs.retainAll(CLI_ONLY_ARGS);
        return commandLineOnlyArgs;
    }
}
