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

package me.proton.test.fusion.utils

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

/**
 * Provides top activity from activity stack in [Stage.RESUMED] state.
 */
object ActivityProvider {
    val currentActivity: Activity? get() = getActivity()

    private fun getActivity(): Activity? {
        val currentActivity = arrayOfNulls<Activity>(1)
        getInstrumentation().runOnMainSync {
            val activities = ActivityLifecycleMonitorRegistry
                .getInstance()
                .getActivitiesInStage(Stage.RESUMED)

            if (activities.iterator().hasNext()) {
                currentActivity[0] = activities.iterator().next() as Activity
            }
        }
        return currentActivity[0]
    }

    private fun getActivity(stage: Stage): Activity? {
        val currentActivity = arrayOfNulls<Activity>(1)
        getInstrumentation().runOnMainSync {
            val activities = ActivityLifecycleMonitorRegistry
                .getInstance()
                .getActivitiesInStage(stage)

            if (activities.iterator().hasNext()) {
                currentActivity[0] = activities.iterator().next() as Activity
            }
        }
        return currentActivity[0]
    }
}
