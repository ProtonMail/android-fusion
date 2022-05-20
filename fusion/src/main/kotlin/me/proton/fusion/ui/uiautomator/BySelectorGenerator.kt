/*
 * Copyright (c) 2021 Proton Technologies AG
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

package me.proton.fusion.ui.uiautomator

import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiObject2
import java.util.regex.Pattern

/**
 * Generates selector for [UiObject2] element.
 */
@Suppress("UNCHECKED_CAST", "UNUSED_EXPRESSION")
open class BySelectorGenerator<T> {

    protected var objectSelector: BySelector? = null

    private fun addSelector(selector: BySelector): T {
        if (objectSelector != null) {
            objectSelector.apply { selector }
        } else {
            objectSelector = selector
        }
        return this as T
    }

    fun instanceOf(clazz: Class<*>?): T = addSelector(By.clazz(clazz))

    fun isCheckable(): T = addSelector(By.checkable(true))

    fun isNotCheckable(): T = addSelector(By.checkable(false))

    fun isChecked(): T = addSelector(By.checked(true))

    fun isNotChecked(): T = addSelector(By.checked(false))

    fun isDisabled(): T = addSelector(By.enabled(false))

    fun isEnabled(): T = addSelector(By.enabled(true))

    fun isFocusable(): T = addSelector(By.focusable(true))

    fun isNotFocusable(): T = addSelector(By.focusable(false))

    fun isFocused(): T = addSelector(By.focused(true))

    fun isNotFocused(): T = addSelector(By.focused(false))

    fun withClassName(text: String?): T = addSelector(By.clazz(text))

    fun withClassNameMatches(pattern: Pattern?): T = addSelector(By.clazz(pattern))

    fun withContentDesc(text: String): T = addSelector(By.desc(text))

    fun withContentDescContains(text: String): T = addSelector(By.descContains(text))

    fun withContentDescMatches(pattern: Pattern): T = addSelector(By.desc(pattern))

    fun withContentDescStartsWith(text: String): T = addSelector(By.descStartsWith(text))

    fun withContentDescEndsWith(text: String): T = addSelector(By.descEndsWith(text))

    fun withText(text: String): T = addSelector(By.text(text))

    fun withTextMatches(pattern: Pattern): T = addSelector(By.text(pattern))

    fun containsText(text: String): T = addSelector(By.textContains(text))

    fun startsWith(text: String): T = addSelector(By.textStartsWith(text))

    fun endsWith(text: String): T = addSelector(By.textEndsWith(text))

    fun isSelected(): T = addSelector(By.selected(true))

    fun isNotSelected(): T = addSelector(By.selected(false))

    fun isScrollable(): T = addSelector(By.scrollable(true))

    fun isNotScrollable(): T = addSelector(By.scrollable(false))

    fun isClickable(): T = addSelector(By.clickable(true))

    fun isNotClickable(): T = addSelector(By.clickable(false))

    fun isLongClickable(): T = addSelector(By.longClickable(true))

    fun isNotLongClickable(): T = addSelector(By.longClickable(false))

    fun withPkg(pkgName: String): T = addSelector(By.pkg(pkgName))

    fun withPkgMatches(pattern: Pattern): T = addSelector(By.pkg(pattern))

    fun withResName(text: String): T = addSelector(By.res(text))

    fun withResId(packageName: String, resId: String): T = addSelector(By.res(packageName, resId))

    /**
     * Constructs a new {@link BySelector} and sets the resource id criteria.
     *
     * @see BySelector#res(Pattern)
     */
    fun withResIdMatches(pattern: Pattern): T = addSelector(By.res(pattern))

    /**
     * Descendant selectors.
     */
    fun hasDescendant(byObject: ByObject): T =
        addSelector(By.hasDescendant(byObject.objectSelector))

    /**
     * Child selector.
     */
    fun hasChild(byObject: ByObject): T = addSelector(By.hasChild(byObject.objectSelector))
}
