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

package me.proton.fusion.ui.test

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.test.espresso.matcher.ViewMatchers
import me.proton.fusion.ui.compose.builders.OnNode
import me.proton.fusion.ui.espresso.builders.OnView
import org.hamcrest.core.AllOf
import org.junit.Test

class MatchersTests {
    @Test
    fun canAddSemanticMatcher() {
        val node = OnNode()
        node.addSemanticMatcher(hasTestTag("test"))
        assert(node.matchers.count() == 1)
    }

    @Test
    fun builderFunctionsAddSemanticsMatchers() {
        val node2 = OnNode().withText("text")
        val node = OnNode()
            .withAnyTag("")
            .isCheckable()
            .isClickable()
            .hasAncestor(node2)
            .hasChild(node2)
        assert(node.matchers.count() == 5)
    }

    @Test
    fun finalMatcherMatchesAddedSemanticsMatchers() {
        val expectedFinalMatcher = hasText("text") and hasTestTag("Tag")
        val node = OnNode().withText("text").withTag("Tag")
        assert(node.finalMatcher.description == expectedFinalMatcher.description)
    }

    @Test
    fun canAddViewMatcher() {
        val view = OnView()
        view.addViewMatcher(ViewMatchers.isSelected())
        assert(view.matchers.count() == 1)
    }

    @Test
    fun builderFunctionsAddViewMatchers() {
        val view2 = OnView().withText("text")
        val view = OnView()
            .withTag("")
            .isNotChecked()
            .isClickable()
            .hasAncestor(view2)
            .hasChild(view2)
        assert(view.matchers.count() == 5)
    }

    @Test
    fun finalMatcherMatchesAddedViewMatchers() {
        val expectedFinalMatcher =
            AllOf.allOf(ViewMatchers.withText("text"), ViewMatchers.withContentDescription("Any"))
        val node = OnView().withText("text").withContentDesc("Any")
        assert(node.finalMatcher.toString() == expectedFinalMatcher.toString())
    }
}