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
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.UastVisibility
import java.util.EnumSet
import java.util.Locale

class RobotReturnTypeDetector : RobotDetector() {

    override fun onRobotClass(node: UClass, context: JavaContext) {
        node.methods.filter { !it.hasValidRobotReturnType() && !it.isConstructor }
            .forEach { method ->
                context.reportIssue(
                    ISSUE,
                    node,
                    context.getLocation(method),
                    method.replacementFix()
                )
            }
    }

    private fun UMethod.hasValidRobotReturnType(): Boolean {
        if (visibility != UastVisibility.PUBLIC)
            return true

        val returnsRobot =
            returnType?.superTypes?.any { it.canonicalText == "me.proton.test.fusion.data.Robot" } ?: false
        val returnsUnit = returnType?.canonicalText == "void"
        return returnsRobot || returnsUnit
    }

    private fun UMethod.replacementFix(): LintFix {
        val returnType = returnTypeReference?.type?.presentableText?.capitalized()
        val returnTypePattern = if (returnType != null && returnType != "Unit") ": $returnType" else ""
        return LintFix.create()
            .replace()
            .text(returnTypePattern)
            .with("")
            .build()
    }

    companion object {
        val ISSUE: Issue = Issue.create(
            id = "ProhibitedRobotObjectReturnType",
            briefDescription = "Ensures public functions of Robot classes return only Robot or Unit",
            explanation = """
                For classes implementing the Robot interface, functions should return either:
                - Objects that extend the Robot interface
                - Unit
            """,
            category = Category.COMPLIANCE,
            priority = 6,
            severity = Severity.FATAL,
            implementation = Implementation(
                RobotReturnTypeDetector::class.java,
                EnumSet.of(Scope.TEST_SOURCES, Scope.JAVA_FILE)
            )
        )
    }
}

private fun String.capitalized(): String =
    replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

