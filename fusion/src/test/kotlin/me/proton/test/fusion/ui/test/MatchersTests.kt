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

package me.proton.test.fusion.ui.test

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.test.espresso.matcher.ViewMatchers
import me.proton.test.fusion.ui.compose.builders.OnNode
import me.proton.test.fusion.ui.compose.builders.OnNodes
import me.proton.test.fusion.ui.espresso.builders.OnView
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
        assert(node.matcher.description == expectedFinalMatcher.description)
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

    @Test
    fun useUnmergedTree() {
        assert(OnNode().withText("").useUnmergedTree(true).shouldUseUnmergedTree)
        assert(OnNodes().withText("").useUnmergedTree(true).shouldUseUnmergedTree)
    }
}
