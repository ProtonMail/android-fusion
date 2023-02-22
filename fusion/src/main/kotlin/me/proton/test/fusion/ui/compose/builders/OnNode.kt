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

package me.proton.test.fusion.ui.compose.builders

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import me.proton.test.fusion.FusionConfig.Compose
import me.proton.test.fusion.FusionConfig.Compose.testRule
import me.proton.test.fusion.ui.compose.wrappers.NodeActions
import me.proton.test.fusion.ui.compose.wrappers.NodeMatchers

class OnNode(
    private val overrideInteraction: SemanticsNodeInteraction? = null,
    private val shouldUseUnmergedTree: Boolean = Compose.useUnmergedTree.get()
) : NodeMatchers<OnNode>(), NodeActions {
    override fun addSemanticMatcher(matcher: SemanticsMatcher): OnNode =
        apply { matchers.add(matcher) }

    override val interaction
        get() =
            overrideInteraction ?: testRule.get().onNode(finalMatcher, shouldUseUnmergedTree)
}
