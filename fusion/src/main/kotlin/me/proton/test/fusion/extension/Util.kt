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

package me.proton.test.fusion.extension

import android.util.Log
import me.proton.test.fusion.FusionConfig


fun MutableSet<Throwable>.warnIfMultiple(): MutableSet<Throwable> = apply {
    if (count() > 1) {
        Log.w(FusionConfig.fusionTag, "Multiple exception caught during verification")
        forEach {
            Log.w(FusionConfig.fusionTag, it)
        }
    }
}

fun Throwable.handleErrorLog() {
    // Relevant information is usually on the second line of AssertionError message
    // In other cases it's the first line
    val messageLine = if (this is AssertionError && message?.lines()?.count()!! > 1) 1 else 0
    message
        ?.lines()
        ?.get(messageLine)
        .let {
            Log.d(FusionConfig.fusionTag, "Condition was not met immediately: $it")
        }
}