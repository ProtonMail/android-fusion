/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 by Proton Technologies A.G. (Switzerland) Email: contact@protonmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
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

