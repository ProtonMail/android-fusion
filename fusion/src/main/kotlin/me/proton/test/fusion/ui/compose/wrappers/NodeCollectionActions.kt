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

package me.proton.test.fusion.ui.compose.wrappers

import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import me.proton.test.fusion.ui.compose.ComposeWaiter.waitFor as wait
import me.proton.test.fusion.ui.compose.builders.OnNode
import me.proton.test.fusion.ui.compose.builders.OnNodes

typealias NodeCollectionInteraction =
    ComposeTestRule.() -> SemanticsNodeInteractionCollection

interface NodeCollectionActions : ComposeInteraction<SemanticsNodeInteractionCollection> {
    override val composeInteraction: NodeCollectionInteraction
        get() = { onAllNodes(matcher, shouldUseUnmergedTree) }

    /** return node at [position] **/
    fun atPosition(position: Int) = OnNode(
        interaction[position]
    )

    /** return first matched node **/
    fun onFirst() = OnNode(interaction.onFirst())

    /** return last matched node **/
    fun onLast() = OnNode(interaction.onLast())

    /** filter all nodes to one by [node] **/
    fun filterToOne(node: OnNode) = OnNode(interaction.filterToOne(node.matcher))

    /** filter all nodes by [node] **/
    fun filter(node: OnNode) = OnNodes(interaction.filter(node.matcher))

    /** Node assertions **/
    /** check all nodes match [node] matcher **/
    fun assertEach(node: OnNode) =
        waitFor {
            interaction.assertAll(node.matcher)
        }

    /** check any node matches [node] matcher **/
    fun assertAny(node: OnNode) =
        waitFor {
            interaction.assertAny(node.matcher)
        }

    /** check not collection [count] **/
    fun assertCountEquals(count: Int) =
        waitFor {
            interaction.assertCountEquals(count)
        }

    private fun waitFor(block: () -> Any) = wait(block = block) as NodeCollectionActions
}
