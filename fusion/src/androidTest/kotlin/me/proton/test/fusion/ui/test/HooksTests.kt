package me.proton.test.fusion.ui.test

import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.ui.espresso.EspressoWaiter
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class HooksTests {
    private var onSuccessCalled = false
    private var beforeCalled = false
    private var afterCalled = false
    private var onFailureCalled = false

    private val espressoWaiter: EspressoWaiter = object : EspressoWaiter { }

    @Before
    fun setUp() {
        FusionConfig.Espresso.waitTimeout.set(0.seconds)

        FusionConfig.Espresso.onFailure = { let { onFailureCalled = true  } }
        FusionConfig.Espresso.onSuccess = { let { onSuccessCalled = true } }
        FusionConfig.Espresso.before = { let { beforeCalled = true } }
        FusionConfig.Espresso.after = { let { afterCalled = true } }
    }

    @After
    fun reset() {
        onSuccessCalled = false
        beforeCalled = false
        afterCalled = false
        onFailureCalled = false
    }

    @Test
    fun failureHooks() {
        with(espressoWaiter) {
            try {
                waitFor {
                    assert(false)
                }
            } catch (e: AssertionError) {
                assert(onFailureCalled)
                assert(afterCalled)
                assert(beforeCalled)
                assert(!onSuccessCalled)
            }
        }
    }

    @Test
    fun successHooks() {
        with(espressoWaiter) {
            waitFor {
                assert(true)
            }
            assert(onSuccessCalled)
            assert(afterCalled)
            assert(beforeCalled)
            assert(!onFailureCalled)
        }
    }
}