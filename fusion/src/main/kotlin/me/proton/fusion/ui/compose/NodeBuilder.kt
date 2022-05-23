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

package me.proton.fusion.ui.compose

import androidx.annotation.StringRes
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.*
import androidx.compose.ui.text.input.ImeAction
import me.proton.fusion.FusionConfig
import me.proton.fusion.utils.StringUtils

/**
 * Provides the API for Node [SemanticsMatcher] generation.
 */
open class NodeBuilder<T> {
    var shouldUseUnmergedTree: Boolean = false
    var timeoutMillis: Long = 10_000L

    private val semanticsMatchers = ArrayList<SemanticsMatcher>()

    @Suppress("UNCHECKED_CAST")
    private fun addSemanticMatcher(matcher: SemanticsMatcher): T {
        semanticsMatchers.add(matcher)
        return this as T
    }

    /** Settings wrappers **/
    @Suppress("UNCHECKED_CAST")
    fun withUnmergedTree(): T {
        shouldUseUnmergedTree = true
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withTimeout(timeout: Long): T {
        timeoutMillis = timeout
        return this as T
    }

    /** Final semantic matcher */
    fun semanticMatcher(): SemanticsMatcher {
        try {
            var finalSemanticMatcher = semanticsMatchers.first()
            semanticsMatchers.drop(1).forEach {
                finalSemanticMatcher = finalSemanticMatcher.and(it)
            }
            return finalSemanticMatcher
        } catch (ex: NoSuchElementException) {
            throw AssertionError("At least one matcher should be provided to operate on the node.")
        }
    }

    /** Node filters **/
    fun withText(text: String) =
        addSemanticMatcher(hasText(text, substring = false, ignoreCase = false))

    fun withText(@StringRes textId: Int): T =
        addSemanticMatcher(
            hasText(
                StringUtils.stringFromResource(textId),
                substring = false,
                ignoreCase = false
            )
        )

    fun containsText(@StringRes textId: Int): T =
        addSemanticMatcher(
            hasText(
                StringUtils.stringFromResource(textId),
                substring = true,
                ignoreCase = false
            )
        )

    fun containsText(text: String): T =
        addSemanticMatcher(hasText(text, substring = true, ignoreCase = false))

    fun containsTextIgnoringCase(text: String): T =
        addSemanticMatcher(hasText(text, substring = true, ignoreCase = true))

    fun withTextIgnoringCase(text: String) =
        addSemanticMatcher(hasText(text, substring = false, ignoreCase = true))

    fun withTextIgnoringCase(@StringRes textId: Int) =
        addSemanticMatcher(
            hasText(
                StringUtils.stringFromResource(textId),
                substring = false,
                ignoreCase = true
            )
        )

    fun withContentDesc(
        contentDescriptionText: String,
        substring: Boolean = false,
        ignoreCase: Boolean = true
    ) =
        addSemanticMatcher(hasContentDescription(contentDescriptionText, substring, ignoreCase))

    /** withContentDesc filters **/
    fun withContentDesc(
        @StringRes contentDescriptionTextId: Int,
        substring: Boolean = false,
        ignoreCase: Boolean = true
    ): T = withContentDesc(
        StringUtils.stringFromResource(
            contentDescriptionTextId,
            substring,
            ignoreCase
        )
    )

    fun withContentDescContains(@StringRes textId: Int): T =
        withContentDesc(
            StringUtils.stringFromResource(textId),
            substring = true,
            ignoreCase = false
        )

    fun withContentDescContains(contentDescriptionText: String): T =
        withContentDesc(contentDescriptionText, substring = true, ignoreCase = false)

    fun withTag(tag: String): T =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.TestTag, tag))

    fun withProgressBarRangeInfo(rangeInfo: ProgressBarRangeInfo) =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ProgressBarRangeInfo,
                rangeInfo
            )
        )

    fun isClickable(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsActions.OnClick))

    fun isNotClickable(): T =
        addSemanticMatcher(SemanticsMatcher.keyNotDefined(SemanticsActions.OnClick))

    fun isChecked(): T =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ToggleableState,
                ToggleableState.On
            )
        )

    fun isNotChecked(): T =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ToggleableState,
                ToggleableState.Off
            )
        )

    fun isCheckable(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.ToggleableState))

    fun isEnabled(): T =
        addSemanticMatcher(!SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled))

    fun isDisabled(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled))

    fun isFocusable(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Focused))

    fun isNotFocusable(): T =
        addSemanticMatcher(SemanticsMatcher.keyNotDefined(SemanticsProperties.Focused))

    fun isFocused(): T =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Focused, true))

    fun isNotFocused(): T =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Focused, false))

    fun isSelected(): T =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Selected, true))

    fun isNotSelected(): T =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Selected, false))

    fun isSelectable(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected))

    fun isScrollable(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsActions.ScrollBy))

    fun isNotScrollable(): T =
        addSemanticMatcher(SemanticsMatcher.keyNotDefined(SemanticsActions.ScrollBy))

    fun isHeading(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))

    fun isDialog(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsDialog))

    fun isPopup(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsPopup))

    fun isRoot(): T = addSemanticMatcher(SemanticsMatcher("isRoot") { it.isRoot })

    fun hasStateDescription(stateDescription: String) =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription,
                stateDescription
            )
        )

    fun hasImeAction(imeAction: ImeAction) =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.ImeAction, imeAction))

    fun hasSetTextAction(): T =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsActions.SetText))

    fun isDescendantOf(ancestorNode: OnNode) =
        addSemanticMatcher(hasAnyAncestor(ancestorNode.semanticMatcher()))

    fun hasParent(parentNode: OnNode) = addSemanticMatcher(parentNode.semanticMatcher())

    fun hasChild(childNode: OnNode) = addSemanticMatcher(hasAnyChild(childNode.semanticMatcher()))

    fun hasSibling(siblingNode: OnNode) =
        addSemanticMatcher(hasAnySibling(siblingNode.semanticMatcher()))

    fun hasDescendant(descendantNode: OnNode) =
        addSemanticMatcher(hasAnyDescendant(descendantNode.semanticMatcher()))

    /** Helpers **/

    fun handlePrint(action: () -> Any) {
        try {
            action()
        } catch (e: AssertionError) {
            if (FusionConfig.compose.shouldPrintHierarchyOnFailure) {
                FusionConfig.compose.testRule.onRoot().printToLog(FusionConfig.fusionTag)
            }
            throw e
        }
    }
}
