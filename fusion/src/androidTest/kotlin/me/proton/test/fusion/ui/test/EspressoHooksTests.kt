/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonCore.
 *
 * ProtonCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonCore.  If not, see <https://www.gnu.org/licenses/>.
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