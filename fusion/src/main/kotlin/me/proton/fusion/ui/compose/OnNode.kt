package me.proton.fusion.ui.compose

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import me.proton.fusion.FusionConfig.Compose

class OnNode(
    private val overrideInteraction: SemanticsNodeInteraction? = null,
    private val shouldUseUnmergedTree: Boolean = Compose.useUnmergedTree.get()
) : NodeMatchers<OnNode>(), NodeActions {
    override fun addSemanticMatcher(matcher: SemanticsMatcher): OnNode = apply { matchers.add(matcher) }
    override val interaction get() = overrideInteraction ?: composeTestRule.onNode(finalMatcher, shouldUseUnmergedTree)
}
