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

package me.proton.test.fusion.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import me.proton.test.fusion.lint.detectors.RobotClassDetector
import me.proton.test.fusion.lint.detectors.RobotReturnTypeDetector

class RobotIssueRegistry : IssueRegistry() {
    override val issues: List<Issue> = listOf(
        RobotClassDetector.ISSUE,
        RobotReturnTypeDetector.ISSUE
    )

    override val api: Int get() = CURRENT_API

    override val vendor: Vendor = Vendor(vendorName = "Fusion")
}
