package me.proton.test.fusion.ui.compose.wrappers

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import me.proton.test.fusion.FusionConfig

interface ComposeInteraction<T> {
    val finalMatcher: SemanticsMatcher
    var shouldUseUnmergedTree: Boolean
    val overrideInteraction: T?
    val composeInteraction: ComposeContentTestRule.() -> T

    val interaction: T
        get() = overrideInteraction ?: FusionConfig
            .Compose
            .testRule
            .get()
            .composeInteraction()
}
