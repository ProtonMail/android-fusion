/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonCore.
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

package me.proton.test.fusion.ui.compose.wrappers

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeTestRule
import me.proton.test.fusion.FusionConfig

interface ComposeInteraction<T> {
    val matcher: SemanticsMatcher
    var shouldUseUnmergedTree: Boolean
    val overrideInteraction: T?
    val composeInteraction: ComposeTestRule.() -> T

    val interaction: T
        get() = overrideInteraction ?: FusionConfig
            .Compose
            .testRule
            .get()
            .composeInteraction()
}
