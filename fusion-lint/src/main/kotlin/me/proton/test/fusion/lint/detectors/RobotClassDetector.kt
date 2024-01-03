/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton AG and ProtonCore.
 *
 * ProtonCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.proton.test.fusion.lint.detectors

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope

import com.android.tools.lint.detector.api.Severity
import com.intellij.psi.PsiModifier
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import java.util.EnumSet

class RobotClassDetector : RobotDetector() {
    override fun onRobotClass(node: UClass, context: JavaContext) {
        if (node.constructors.any { it.hasModifierProperty(PsiModifier.PUBLIC) }) {
            context.reportIssue(
                ISSUE,
                node,
                context.getLocation(node as UElement),
                LintFix.create()
                    .replace()
                    .text("class ${node.name}")
                    .with("object ${node.name}")
                    .build()
            )
        }
    }

    companion object {
        val ISSUE: Issue = Issue.create(
            id = "RobotIsAClass",
            briefDescription = "Prohibits using Robots as classes",
            explanation = "Only objects can be used to implement Robot interface",
            category = Category.COMPLIANCE,
            priority = 4,
            severity = Severity.WARNING,
            implementation = Implementation(
                RobotClassDetector::class.java,
                EnumSet.of(Scope.TEST_SOURCES, Scope.JAVA_FILE)
            )
        )
    }
}