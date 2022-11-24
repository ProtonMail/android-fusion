package me.proton.fusion.ui.compose

import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface NodeCollectionActions: ComposeWaiter {
    val interaction: SemanticsNodeInteractionCollection

    /** return node at [position] **/
    fun atPosition(position: Int) = OnNode(interaction[position])

    /** return first matched node **/
    fun onFirst() = OnNode(interaction.onFirst())

    /** return last matched node **/
    fun onLast() = OnNode(interaction.onLast())

    /** filter all nodes to one by [node] **/
    fun filterToOne(node: OnNode) = OnNode(interaction.filterToOne(node.finalMatcher))

    /** filter all nodes by [node] **/
    fun filter(node: OnNode) =
        waitFor {
            interaction.filter(node.finalMatcher)
        }

    /** Node assertions **/
    /** check all nodes match [node] matcher **/
    fun assertEach(node: OnNode) =
        waitFor {
            interaction.assertAll(node.finalMatcher)
        }

    /** check any node matches [node] matcher **/
    fun assertAny(node: OnNode) =
        waitFor {
            interaction.assertAny(node.finalMatcher)
        }

    /** check not collection [count] **/
    fun assertCountEquals(count: Int) =
        waitFor {
            interaction.assertCountEquals(count)
        }
}