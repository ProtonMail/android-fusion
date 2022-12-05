package me.proton.fusion.ui.compose

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import me.proton.fusion.FusionConfig
import org.junit.Before
import org.junit.Rule

interface FusionComposeTest {
    @get:Rule
    val composeRule: ComposeContentTestRule

    @Before
    fun setComposeRule() {
        FusionConfig.Compose.testRule.set(composeRule)
    }
}