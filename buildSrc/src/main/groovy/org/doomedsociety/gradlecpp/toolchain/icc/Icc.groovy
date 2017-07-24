package org.doomedsociety.gradlecpp.toolchain.icc

import org.gradle.nativeplatform.toolchain.GccCompatibleToolChain

public interface Icc extends GccCompatibleToolChain {
    /**
     * The path required for executing the tool chain.
     * These are used to locate tools for this tool chain, and are prepended to the system PATH when executing these tools.
     */
    List<File> getPath();

    /**
     * Append an entry or entries to the tool chain path.
     *
     * @param pathEntries The path values to append. These are evaluated as per {@link org.gradle.api.Project#files(Object...)}
     */
    void path(Object... pathEntries);
}
