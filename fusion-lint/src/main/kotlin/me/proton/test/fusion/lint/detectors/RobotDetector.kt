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

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.TextFormat
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import java.util.EnumSet

abstract class RobotDetector : Detector(), SourceCodeScanner {

    abstract fun onRobotClass(node: UClass, context: JavaContext)

    override fun createUastHandler(context: JavaContext): UElementHandler =
        object : UElementHandler() {
            override fun visitClass(node: UClass) {
                if (node.superTypes.any { it.canonicalText == "me.proton.test.fusion.data.Robot" }) {
                    println(node.qualifiedName)
                    onRobotClass(node, context)
                }
            }
        }

    fun JavaContext.reportIssue(issue: Issue, node: UElement, location: Location, fix: LintFix) =
        report(
            issue,
            node,
            location,
            issue.getExplanation(TextFormat.TEXT),
            fix
        )

    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UClass::class.java)
}