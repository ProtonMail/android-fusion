/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton AG and ProtonCore.
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

package me.proton.test.fusion.ui.espresso.extensions

import java.util.Calendar
import java.util.Date

val Date.asYear: Int get() = Calendar.getInstance()
    .apply { time = this@asYear }
    .get(Calendar.YEAR)

val Date.asMonth: Int get() = Calendar.getInstance()
    .apply { time = this@asMonth }
    .get(Calendar.MONTH)

val Date.asDayOfMonth: Int get() = Calendar.getInstance()
    .apply { time = this@asDayOfMonth }
    .get(Calendar.DAY_OF_MONTH)
