package org.doomedsociety.gradlecpp.msvc

import org.doomedsociety.gradlecpp.GradleCppUtils
import org.gradle.api.Task
import org.gradle.language.nativeplatform.tasks.AbstractNativeCompileTask
import org.gradle.nativeplatform.NativeBinarySpec

class GradleMsvcUtils {
    static class PCHInfo {
        AbstractNativeCompileTask pchTask
        List<AbstractNativeCompileTask> compileTasks = []
    }

    static PCHInfo getPchCompileTask(NativeBinarySpec binary, String sourceSetName) {
        PCHInfo res = new PCHInfo()
        def linkTask = GradleCppUtils.getLinkTask(binary)
        linkTask.taskDependencies.getDependencies(linkTask).each { Task t ->
            if (t instanceof AbstractNativeCompileTask) {
                if (t.name.toUpperCase().endsWith(sourceSetName.toUpperCase())) {
                    if (res.pchTask) {
                        throw new RuntimeException("Binary ${binary.name} contains >1 PCH compile tasks")
                    }
                    res.pchTask = t
                } else {
                    res.compileTasks << t
                }
            }
        }

        return res.pchTask ? res : null
    }
}
