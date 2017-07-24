package org.doomedsociety.gradlecpp.toolchain.icc

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.operations.BuildOperationProcessor
import org.gradle.internal.os.OperatingSystem
import org.gradle.internal.reflect.Instantiator
import org.gradle.internal.service.ServiceRegistry
import org.gradle.model.Defaults
import org.gradle.model.RuleSource
import org.gradle.nativeplatform.plugins.NativeComponentPlugin
import org.gradle.nativeplatform.toolchain.internal.NativeToolChainRegistryInternal
import org.gradle.nativeplatform.toolchain.internal.gcc.version.CompilerMetaDataProviderFactory
import org.gradle.process.internal.ExecActionFactory

@CompileStatic @TypeChecked
class IccCompilerPlugin implements Plugin<Project> {

    public void apply(Project project) {
        project.getPluginManager().apply(NativeComponentPlugin.class);
    }

    static class Rules extends RuleSource {
        @Defaults
        public static void addToolChain(NativeToolChainRegistryInternal toolChainRegistry, ServiceRegistry serviceRegistry) {
            final FileResolver fileResolver = serviceRegistry.get(FileResolver.class);
            final ExecActionFactory execActionFactory = serviceRegistry.get(ExecActionFactory.class);
            final Instantiator instantiator = serviceRegistry.get(Instantiator.class);
            final BuildOperationProcessor buildOperationProcessor = serviceRegistry.get(BuildOperationProcessor.class);
            final CompilerMetaDataProviderFactory metaDataProviderFactory = serviceRegistry.get(CompilerMetaDataProviderFactory.class);

            toolChainRegistry.registerFactory(Icc.class, new NamedDomainObjectFactory<Icc>() {
                public Icc create(String name) {
                    return instantiator.newInstance(IccToolchain.class, name, buildOperationProcessor, OperatingSystem.current(), fileResolver, execActionFactory, metaDataProviderFactory, instantiator);
                }
            });
            toolChainRegistry.registerDefaultToolChain(IccToolchain.DEFAULT_NAME, Icc.class);
        }

    }
}
