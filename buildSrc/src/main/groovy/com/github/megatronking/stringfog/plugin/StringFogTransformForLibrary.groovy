package com.github.megatronking.stringfog.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.api.BaseVariant
import com.google.common.collect.ImmutableSet
import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
/**
 * StringFog transform used in library.
 *
 * @author Megatron King
 * @since 17/7/28 12:28
 */

class StringFogTransformForLibrary extends StringFogTransform {

    StringFogTransformForLibrary(Project project, DomainObjectSet<BaseVariant> variants) {
        super(project, variants)
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return ImmutableSet.of(
                QualifiedContent.Scope.PROJECT
        )
    }

}
