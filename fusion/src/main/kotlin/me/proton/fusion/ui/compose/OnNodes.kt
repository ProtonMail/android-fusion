package me.proton.fusion.ui.compose

import androidx.compose.ui.test.SemanticsMatcher
import me.proton.fusion.FusionConfig.Compose

open class OnNodes(
    private val shouldUseUnmergedTree: Boolean = Compose.useUnmergedTree.get()
) : NodeMatchers<OnNodes>(), NodeCollectionActions {
    override fun addSemanticMatcher(matcher: SemanticsMatcher): OnNodes = apply { matchers.add(matcher) }
    override val interaction get() = composeTestRule.onAllNodes(finalMatcher, shouldUseUnmergedTree)
}
