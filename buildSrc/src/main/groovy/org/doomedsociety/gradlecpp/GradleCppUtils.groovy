package org.doomedsociety.gradlecpp

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.SystemUtils
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.project.AbstractProject
import org.gradle.language.c.tasks.CCompile
import org.gradle.language.cpp.tasks.CppCompile
import org.gradle.language.nativeplatform.tasks.AbstractNativeCompileTask
import org.gradle.model.internal.core.DirectNodeModelAction
import org.gradle.model.internal.core.ModelActionRole
import org.gradle.model.internal.core.ModelPath
import org.gradle.model.internal.core.ModelReference
import org.gradle.model.internal.core.MutableModelNode
import org.gradle.model.internal.core.rule.describe.ModelRuleDescriptor
import org.gradle.model.internal.core.rule.describe.SimpleModelRuleDescriptor
import org.gradle.model.internal.registry.ModelRegistry
import org.gradle.nativeplatform.NativeBinarySpec
import org.gradle.nativeplatform.NativeExecutableBinarySpec
import org.gradle.nativeplatform.SharedLibraryBinarySpec
import org.gradle.nativeplatform.StaticLibraryBinarySpec
import org.gradle.nativeplatform.internal.AbstractNativeBinarySpec

import java.nio.file.Files

class GradleCppUtils {
    static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS
    }

    static File changeFileExt(File f, String newExt) {
        def fPath = f.absolutePath
        return new File(FilenameUtils.getFullPath(fPath) + FilenameUtils.getBaseName(fPath) + newExt)
    }

    static Task getLinkTask(NativeBinarySpec binary) {
        if (binary.buildTask == null) {
            throw new RuntimeException("BuldTask for ${binary.name} is not created yet; move your action to project.afterEvaluate")
        }

        def binImpl = binary as AbstractNativeBinarySpec
        def taskName = binImpl.namingScheme.getTaskName((binary instanceof StaticLibraryBinarySpec) ? 'create' : 'link')
        def linkTask = binary.tasks.find { t -> t.name == taskName}

        if (linkTask == null) {
            throw new RuntimeException("Unable to find link task for ${binary.name}")
        }

        return linkTask
    }

    static List<Task> getCompileTasks(NativeBinarySpec binary) {
        def linkTask = getLinkTask(binary)

        List<Task> res = []
        linkTask.taskDependencies.getDependencies(linkTask).each { Task t ->
            switch (t) {
                case CppCompile:
                case CCompile:
                    res << t;
                    break;
            }
        }

        return res
    }

    static void copyFile(String srcPath, String dstPath, boolean overwrite) {
        copyFile(new File(srcPath), new File(dstPath), overwrite)
    }

    static void copyFile(File src, File dst, boolean overwrite) {
        if (overwrite) {
            dst.delete()
        }
        Files.copy(src.toPath(), dst.toPath())
    }

    static File getBinaryOutputFile(NativeBinarySpec binary) {
        if (binary instanceof SharedLibraryBinarySpec) {
            return (binary as SharedLibraryBinarySpec).sharedLibraryFile
        } else if (binary instanceof NativeExecutableBinarySpec) {
            return (binary as NativeExecutableBinarySpec).executableFile
        }

        return null
    }

    static void onTasksCreated(Project p, String desc, Closure action) {
        ModelRegistry mr = (p as AbstractProject).getModelRegistry()
        def modelPath = ModelPath.path("tasks")
        ModelRuleDescriptor ruleDescriptor = new SimpleModelRuleDescriptor(desc);

        mr.configure(ModelActionRole.Finalize, DirectNodeModelAction.of(ModelReference.of(modelPath), ruleDescriptor, new Action<MutableModelNode>() {
            @Override
            void execute(MutableModelNode node) {
                action()
            }
        }))
    }
}
