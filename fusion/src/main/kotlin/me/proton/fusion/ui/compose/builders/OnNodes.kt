package me.proton.fusion.ui.compose.builders

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import me.proton.fusion.FusionConfig.Compose
import me.proton.fusion.FusionConfig.Compose.testRule
import me.proton.fusion.ui.compose.wrappers.NodeCollectionActions
import me.proton.fusion.ui.compose.wrappers.NodeMatchers

open class OnNodes(
    private val overrideInteraction: SemanticsNodeInteractionCollection? = null,
    private val shouldUseUnmergedTree: Boolean = Compose.useUnmergedTree.get()
) : NodeMatchers<OnNodes>(), NodeCollectionActions {
    override fun addSemanticMatcher(matcher: SemanticsMatcher): OnNodes = apply { matchers.add(matcher) }
    override val interaction
        get() = overrideInteraction ?: testRule.get().onAllNodes(finalMatcher, shouldUseUnmergedTree)
}
