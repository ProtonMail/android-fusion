/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 by Proton Technologies A.G. (Switzerland) Email: contact@protonmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package me.proton.test.fusion.ui.test

import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.ui.FusionWaiter
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class EspressoHooksTests {
    private var onSuccessCalled = false
    private var beforeCalled = false
    private var afterCalled = false
    private var onFailureCalled = false
    private var onFailureCalled2 = false

    private val espressoWaiter: FusionWaiter = object : FusionWaiter { }

    @Before
    fun setUp() {
        FusionConfig.Espresso.waitTimeout.set(3.seconds)

        // reset
        onSuccessCalled = false
        beforeCalled = false
        afterCalled = false
        onFailureCalled = false
        onFailureCalled2 = false


        FusionConfig.Espresso.onFailure = { onFailureCalled = true }
        FusionConfig.Espresso.onFailure = { onFailureCalled2 = true }
        FusionConfig.Espresso.onSuccess = { onSuccessCalled = true }
        FusionConfig.Espresso.before = { beforeCalled = true }
        FusionConfig.Espresso.after = { afterCalled = true }


        assert(!onFailureCalled)
        assert(!onFailureCalled2)
        assert(!afterCalled)
        assert(!beforeCalled)
        assert(!onSuccessCalled)
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
                assert(onFailureCalled2)
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
            assert(!onFailureCalled2)
        }
    }
}