package me.proton.fusion.ui.compose.builders

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import me.proton.fusion.FusionConfig.Compose
import me.proton.fusion.FusionConfig.Compose.testRule
import me.proton.fusion.ui.compose.wrappers.NodeActions
import me.proton.fusion.ui.compose.wrappers.NodeMatchers

class OnNode(
    private val overrideInteraction: SemanticsNodeInteraction? = null,
    private val shouldUseUnmergedTree: Boolean = Compose.useUnmergedTree.get()
) : NodeMatchers<OnNode>(), NodeActions {
    override fun addSemanticMatcher(matcher: SemanticsMatcher): OnNode = apply { matchers.add(matcher) }
    override val interaction get() = overrideInteraction ?: testRule.get().onNode(finalMatcher, shouldUseUnmergedTree)
}