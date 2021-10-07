package com.github.megatronking.stringfog.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.api.BaseVariant
import com.google.common.collect.ImmutableSet
import org.gradle.api.DomainObjectSet
import org.gradle.api.Project

/**
 * StringFog transform used in application.
 *
 * @author Megatron King
 * @since 17/7/28 12:28
 */

class StringFogTransformForApplication extends StringFogTransform {

    StringFogTransformForApplication(Project project, DomainObjectSet<BaseVariant> variants) {
        super(project, variants);
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        def name = QualifiedContent.Scope.PROJECT_LOCAL_DEPS.name()
        def deprecated = QualifiedContent.Scope.PROJECT_LOCAL_DEPS.getClass()
                .getField(name).getAnnotation(Deprecated.class)

        if (deprecated == null) {
            return ImmutableSet.<QualifiedContent.Scope> of(QualifiedContent.Scope.PROJECT
                    , QualifiedContent.Scope.PROJECT_LOCAL_DEPS
                    , QualifiedContent.Scope.EXTERNAL_LIBRARIES
                    , QualifiedContent.Scope.SUB_PROJECTS
                    , QualifiedContent.Scope.SUB_PROJECTS_LOCAL_DEPS)
        } else {
            return ImmutableSet.<QualifiedContent.Scope> of(QualifiedContent.Scope.PROJECT
                    , QualifiedContent.Scope.EXTERNAL_LIBRARIES
                    , QualifiedContent.Scope.SUB_PROJECTS)
        }
    }

}
