package org.doomedsociety.gradlecpp.cfg

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

@CompileStatic @TypeChecked
public enum BinaryKind {
    EXECUTABLE,
    SHARED_LIBRARY,
    STATIC_LIBRARY
}