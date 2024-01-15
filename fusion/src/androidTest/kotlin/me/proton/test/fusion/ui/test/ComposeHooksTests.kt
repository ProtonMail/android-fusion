package me.proton.test.fusion.ui.test

import androidx.compose.material.Text
import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.ui.compose.FusionComposeTest
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class ComposeHooksTests : FusionComposeTest() {

    private var onSuccessCalled = false
    private var beforeCalled = false
    private var afterCalled = false
    private var onFailureCalled = false
    private var onFailureCalled2 = false

    @Before
    fun setUp() {
        FusionConfig.Compose.waitTimeout.set(3.seconds)

        // reset
        onSuccessCalled = false
        beforeCalled = false
        afterCalled = false
        onFailureCalled = false
        onFailureCalled2 = false


        FusionConfig.Compose.onFailure = { onFailureCalled = true }
        FusionConfig.Compose.onFailure = { onFailureCalled2 = true }
        FusionConfig.Compose.onSuccess = { onSuccessCalled = true }
        FusionConfig.Compose.before = { beforeCalled = true }
        FusionConfig.Compose.after = { afterCalled = true }


        assert(!onFailureCalled)
        assert(!onFailureCalled2)
        assert(!afterCalled)
        assert(!beforeCalled)
        assert(!onSuccessCalled)
    }

    @Test
    fun failureHooks() {
        withContent {
            Text("text")
        }

        try {
            node.withText("not-text").await {
                assertIsDisplayed()
            }
        } catch (e: AssertionError) {
            assert(onFailureCalled)
            assert(onFailureCalled2)
            assert(afterCalled)
            assert(beforeCalled)
            assert(!onSuccessCalled)
        }
    }

    @Test
    fun successHooks() {
        withContent {
            Text("text")
        }

        node.withText("text").await {
            assertIsDisplayed()
        }

        assert(onSuccessCalled)
        assert(afterCalled)
        assert(beforeCalled)
        assert(!onFailureCalled)
        assert(!onFailureCalled2)
    }
}