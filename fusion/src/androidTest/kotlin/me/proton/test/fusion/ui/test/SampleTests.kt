package me.proton.test.fusion.ui.test

import androidx.compose.material.Text
import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.ui.compose.FusionComposeTest
import org.junit.Test

class SampleTests: FusionComposeTest() {

    @Test
    fun simpleText() {
        val text = "test"
        withContent {
            Text(text)
        }

        node.withText(text).assertIsDisplayed()
    }
}