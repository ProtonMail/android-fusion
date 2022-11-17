package me.proton.fusion.ui.compose

import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.SemanticsMatcher
import me.proton.fusion.FusionConfig

open class OnAllNodes(
    private val useUnmergedTree: Boolean = true,
    override val shouldPrintToLog: Boolean = FusionConfig.Compose.shouldPrintToLog.get(),
    override val shouldPrintHierarchyOnFailure: Boolean = FusionConfig.Compose.shouldPrintHierarchyOnFailure.get(),
) : ComposeSelectable<OnAllNodes> {
    private val interaction
        get() = composeTestRule.onAllNodes(matchers.final, useUnmergedTree)
    override val matchers: ArrayList<SemanticsMatcher> = arrayListOf()

    /** Node selectors **/
    fun atPosition(position: Int) = OnNode(overrideInteraction = interaction[position])
    fun onFirst() = OnNode(overrideInteraction = interaction.onFirst())
    fun onLast() = OnNode(overrideInteraction = interaction.onLast())
    fun filterToOne(node: OnNode) =
        OnNode(overrideInteraction = interaction.filterToOne(node.matchers.final))

    fun filter(node: OnNode) =
        waitFor {
            interaction.filter(node.matchers.final)
        }

    /** Node assertions **/
    fun assertEach(node: OnNode) =
        waitFor {
            interaction.assertAll(node.matchers.final)
        }

    fun assertAny(node: OnNode) =
        waitFor {
            interaction.assertAny(node.matchers.final)
        }

    fun assertCountEquals(count: Int) =
        waitFor {
            interaction.assertCountEquals(count)
        }
}
