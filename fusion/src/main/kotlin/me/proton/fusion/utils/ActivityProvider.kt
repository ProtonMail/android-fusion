package me.proton.fusion.utils

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

