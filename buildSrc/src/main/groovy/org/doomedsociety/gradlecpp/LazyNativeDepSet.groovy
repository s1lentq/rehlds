package org.doomedsociety.gradlecpp

import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.nativeplatform.NativeBinarySpec
import org.gradle.nativeplatform.NativeDependencySet
import org.gradle.nativeplatform.NativeLibraryBinary
import org.gradle.nativeplatform.SharedLibraryBinarySpec
import org.gradle.nativeplatform.StaticLibraryBinarySpec

class LazyNativeDepSet implements NativeDependencySet {
    NativeLibraryBinary binary;
    Closure resolveFunc

    LazyNativeDepSet(Closure resolveFunc) {
        this.resolveFunc = resolveFunc
    }

    public FileCollection getIncludeRoots() {
        return getBinary().getHeaderDirs();
    }

    public FileCollection getLinkFiles() {
        return getBinary().getLinkFiles();
    }

    public FileCollection getRuntimeFiles() {
        return getBinary().getRuntimeFiles();
    }

    public NativeLibraryBinary getBinary() {
        if (binary == null) {
            binary = resolveBinary();
        }

        return binary;
    }

    public NativeLibraryBinary resolveBinary() {
        return (NativeLibraryBinary) resolveFunc()
    }

    public static LazyNativeDepSet create(Closure resolveFunc) {
        return new LazyNativeDepSet(resolveFunc);
    }

    public static LazyNativeDepSet create(Project p, String libName, String buildType, boolean staticLib) {
        return new LazyNativeDepSet({
            List<NativeBinarySpec> candidates = p.binaries.findAll { NativeBinarySpec b ->
                if (staticLib) {
                    if (!(b instanceof StaticLibraryBinarySpec)) return false
                } else {
                    if (!(b instanceof SharedLibraryBinarySpec)) return false
                }

                if (libName != null && b.component.name != libName) return false
                if (buildType != null && b.buildType.name != buildType) return false
                return true
            } as List

            if (candidates.empty) {
                throw new RuntimeException("Could not find library name=${libName}, buildType=${buildType}, static=${staticLib} in project ${p.name}")
            }

            if (candidates.size() > 1) {
                throw new RuntimeException("> 1 candidates found for find library name=${libName}, buildType=${buildType}, static=${staticLib} in project ${p.name}: ${candidates}")
            }

            return candidates[0]
        })
    }
}
