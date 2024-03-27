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