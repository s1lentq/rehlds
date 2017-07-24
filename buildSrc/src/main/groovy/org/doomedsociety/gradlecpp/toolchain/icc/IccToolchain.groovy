package org.doomedsociety.gradlecpp.toolchain.icc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.operations.BuildOperationProcessor
import org.gradle.internal.reflect.Instantiator
import org.gradle.internal.os.OperatingSystem
import org.gradle.nativeplatform.toolchain.internal.gcc.AbstractGccCompatibleToolChain
import org.gradle.nativeplatform.toolchain.internal.gcc.DefaultGccPlatformToolChain
import org.gradle.nativeplatform.toolchain.internal.gcc.version.CompilerMetaDataProviderFactory
import org.gradle.process.internal.ExecActionFactory

@CompileStatic @TypeChecked
class IccToolchain extends AbstractGccCompatibleToolChain implements Icc {
    public static final String DEFAULT_NAME = "icc";

    public IccToolchain(String name, BuildOperationProcessor buildOperationProcessor, OperatingSystem operatingSystem, FileResolver fileResolver, ExecActionFactory execActionFactory, CompilerMetaDataProviderFactory metaDataProviderFactory, Instantiator instantiator) {
        super(name, buildOperationProcessor, operatingSystem, fileResolver, execActionFactory, metaDataProviderFactory.gcc(), instantiator);
    }

    @Override
    protected void configureDefaultTools(DefaultGccPlatformToolChain toolChain) {
        toolChain.getLinker().setExecutable("/opt/intel/bin/icpc");
        toolChain.getStaticLibArchiver().setExecutable("/opt/intel/bin/xiar")
        toolChain.getcCompiler().setExecutable("/opt/intel/bin/icc");
        toolChain.getCppCompiler().setExecutable("/opt/intel/bin/icpc");
        toolChain.getObjcCompiler().setExecutable("/opt/intel/bin/icc");
        toolChain.getObjcppCompiler().setExecutable("/opt/intel/bin/icpc");
        toolChain.getAssembler().setExecutable("as");

    }

    @Override
    protected String getTypeName() {
        return "Icc";

    }

}
