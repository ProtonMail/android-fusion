package me.proton.fusion.ui.compose

import android.util.Log
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.proton.fusion.AndroidComposeRule
import me.proton.fusion.FusionConfig.Compose
import me.proton.fusion.FusionConfig.fusionTag
import me.proton.fusion.utils.StringUtils
import java.util.concurrent.TimeoutException

interface ComposeSelectable<T> {
    val matchers: ArrayList<SemanticsMatcher>
    val composeTestRule: AndroidComposeRule get() = Compose.testRule.get()
    val shouldPrintToLog: Boolean
    val shouldPrintHierarchyOnFailure: Boolean

    val ArrayList<SemanticsMatcher>.final: SemanticsMatcher
        get() {
            require(isNotEmpty()) { "At least one matcher should be provided to operate on the node." }
            var finalSemanticMatcher = first()
            drop(1).forEach {
                finalSemanticMatcher = finalSemanticMatcher.and(it)
            }
            Log.v(
                fusionTag,
                "Final semantic matcher: ${finalSemanticMatcher.description}"
            )
            return finalSemanticMatcher
        }

    /** with **/
    fun withText(text: String, exactly: Boolean = false) =
        addSemanticMatcher(hasText(text, substring = exactly, ignoreCase = !exactly))

    fun withText(textId: Int, exactly: Boolean = false) =
        addSemanticMatcher(
            hasText(
                StringUtils.stringFromResource(textId),
                substring = exactly,
                ignoreCase = !exactly
            )
        )

    fun withContentDescription(contentDescription: String, exactly: Boolean = false) =
        addSemanticMatcher(hasContentDescription(contentDescription, exactly, !exactly))

    fun withContentDescription(textId: Int, exactly: Boolean = false) =
        addSemanticMatcher(
            hasContentDescription(
                StringUtils.stringFromResource(
                    textId,
                    exactly,
                    !exactly
                )
            )
        )

    fun withTag(tag: String) =
        addSemanticMatcher(hasTestTag(tag))

    fun withSiblingText(text: String, exactly: Boolean = false) =
        addSemanticMatcher(hasAnySibling(hasTextExactly(text)))

    /** is **/
    fun isClickable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsActions.OnClick))

    fun isNotClickable() =
        addSemanticMatcher(SemanticsMatcher.keyNotDefined(SemanticsActions.OnClick))

    fun isChecked() =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ToggleableState,
                ToggleableState.On
            )
        )

    fun isNotChecked() =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ToggleableState,
                ToggleableState.Off
            )
        )

    fun isCheckable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.ToggleableState))

    fun isEnabled() =
        addSemanticMatcher(!SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled))

    fun isDisabled() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled))

    fun isFocusable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Focused))

    fun isNotFocusable() =
        addSemanticMatcher(SemanticsMatcher.keyNotDefined(SemanticsProperties.Focused))

    fun isFocused() =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Focused, true))

    fun isNotFocused() =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Focused, false))

    fun isSelected() =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Selected, true))

    fun isNotSelected() =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Selected, false))

    fun isSelectable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected))

    fun isScrollable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsActions.ScrollBy))

    fun isNotScrollable() =
        addSemanticMatcher(SemanticsMatcher.keyNotDefined(SemanticsActions.ScrollBy))

    fun isHeading() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))

    fun isDialog() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsDialog))

    fun isPopup() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsPopup))

    fun isRoot() = addSemanticMatcher(SemanticsMatcher("isRoot") { it.isRoot })

    /** has **/
    fun hasStateDescription(stateDescription: String) =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription,
                stateDescription
            )
        )

    fun hasImeAction(imeAction: ImeAction) =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.ImeAction, imeAction))

    fun hasSetTextAction() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsActions.SetText))

    fun hasAncestor(ancestor: OnNode) =
        addSemanticMatcher(hasAnyAncestor(ancestor.matchers.final))

    fun hasParent(parent: OnNode) =
        addSemanticMatcher(parent.matchers.final)

    fun hasChild(child: OnNode) =
        addSemanticMatcher(hasAnyChild(child.matchers.final))

    fun hasSibling(sibling: OnNode) =
        addSemanticMatcher(hasAnySibling(sibling.matchers.final))

    fun hasDescendant(descendant: OnNode) =
        addSemanticMatcher(hasAnyDescendant(descendant.matchers.final))

    fun T.waitFor(
        timeout: Long = 30_000L,
        interval: Long = 250L,
        block: () -> Any,
    ): T {
        var error: Throwable = TimeoutException("Condition not met in ${timeout}ms")
        try {
            composeTestRule.waitForIdle()
            composeTestRule.waitUntil(timeout) {
                try {
                    block().apply {
                        if (shouldPrintToLog) {
                            when (this) {
                                is SemanticsNodeInteraction -> printToLog(fusionTag)
                                is SemanticsNodeInteractionCollection -> printToLog(fusionTag)
                            }
                        }
                    }

                    true
                } catch (e: AssertionError) {
                    // Thrown on Compose failed actions and assertions
                    error = handleTestError(e, 1)
                    runBlocking { delay(interval) }
                    false
                } catch (e: IllegalStateException) {
                    // Thrown when Compose view is not ready
                    error = handleTestError(e, 0)
                    runBlocking { delay(interval) }
                    false
                }
            }
        } catch (e: ComposeTimeoutException) {
            e.message?.let { Log.e(fusionTag, it) }
            if (shouldPrintHierarchyOnFailure) {
                composeTestRule.onRoot().printToLog(fusionTag)
            }
            throw error
        }
        return this
    }

    private fun handleTestError(throwable: Throwable, messageLine: Int): Throwable {
        val line = throwable.message?.lines()?.get(messageLine).toString()
        Log.i(fusionTag, "Condition not yet met: $line")
        return throwable
    }

    @Suppress("UNCHECKED_CAST")
    private fun addSemanticMatcher(matcher: SemanticsMatcher): T {
        matchers.add(matcher)
        return this as T
    }
}
