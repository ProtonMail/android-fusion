package me.proton.fusion.ui.compose.wrappers

import androidx.annotation.StringRes
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.text.input.ImeAction
import me.proton.fusion.FusionConfig.targetContext
import me.proton.fusion.ui.compose.builders.OnNode

/**
 * A collection of semantic matcher wrappers
 *
 * @param T - [NodeMatchers] implementation to be returned after adding at least 1 matcher
 */
abstract class NodeMatchers<T : NodeMatchers<T>> {
    val matchers: ArrayList<SemanticsMatcher> = arrayListOf()

    /**
     * Add a semantic matcher and return [T] - a [NodeMatchers] implementation
     */
    abstract fun addSemanticMatcher(matcher: SemanticsMatcher): T

    val finalMatcher: SemanticsMatcher
        get() {
            require(matchers.isNotEmpty()) { "At least one matcher should be provided to operate on the node." }
            return matchers.reduce { first, other -> first and other }
        }

    /**
     * 'with' matchers - with argument
     *  checks on dynamic content - eg. text, description
     */

    /** Matches node with [text] **/
    open fun withText(text: String) =
        addSemanticMatcher(hasText(text))

    /** Matches node with [text] substring **/
    open fun withTextSubstring(text: String) =
        addSemanticMatcher(hasText(text, substring = true))

    /** Matches node with [text] ignoring case **/
    open fun withTextIgnoringCase(text: String) =
        addSemanticMatcher(hasText(text, ignoreCase = true))

    /** *Matches node with [text] substring ignoring case **/
    open fun withTextSubstringIgnoringCase(text: String) =
        addSemanticMatcher(hasText(text, substring = true, ignoreCase = true))

    /** Matches node with string resource [textId] **/
    open fun withText(@StringRes textId: Int) =
        withText(targetContext.resources.getString(textId))

    /**  Matches node with string resource [textId] substring **/
    open fun withTextSubstring(@StringRes textId: Int) =
        withTextSubstring(targetContext.resources.getString(textId))

    /** Matches node with string resource [textId] ignoring case **/
    open fun withTextIgnoringCase(@StringRes textId: Int) =
        withTextIgnoringCase(targetContext.resources.getString(textId))

    /** Matches node with string resource [textId] substring ignoring case **/
    open fun withTextSubstringIgnoringCase(@StringRes textId: Int) =
        withTextIgnoringCase(targetContext.resources.getString(textId))

    /** Matches node with [contentDescription] **/
    open fun withContentDescription(contentDescription: String) =
        addSemanticMatcher(hasContentDescription(contentDescription))

    /** Matches node with [contentDescription] substring **/
    open fun withContentDescriptionSubstring(contentDescription: String) =
        addSemanticMatcher(hasContentDescription(contentDescription, substring = true))

    /** Matches node with [contentDescription] ignoring case **/
    open fun withContentDescriptionIgnoringCase(contentDescription: String) =
        addSemanticMatcher(hasContentDescription(contentDescription, ignoreCase = true))

    /** Matches node with [contentDescription] substring ignoring case **/
    open fun withContentDescriptionSubstringIgnoringCase(contentDescription: String) =
        addSemanticMatcher(hasContentDescription(contentDescription, substring = true, ignoreCase = true))

    /** Matches node with content description with string resource [textId] **/
    open fun withContentDescription(@StringRes textId: Int) =
        withContentDescription(targetContext.resources.getString(textId))

    /** Matches node with content description with string resource [textId] substring **/
    open fun withContentDescriptionSubstring(@StringRes textId: Int) =
        withContentDescriptionSubstring(targetContext.resources.getString(textId))

    /** Matches node with content description with string resource [textId] ignoring case **/
    open fun withContentDescriptionIgnoringCase(@StringRes textId: Int) =
        withContentDescriptionIgnoringCase(targetContext.resources.getString(textId))

    /** Matches node with content description with string resource [textId] substring ignoring case **/
    open fun withContentDescriptionSubstringIgnoringCase(@StringRes textId: Int) =
        withContentDescriptionSubstringIgnoringCase(targetContext.resources.getString(textId))

    /** Matches node with test [tag] **/
    open fun withTag(tag: String) =
        addSemanticMatcher(hasTestTag(tag))


    /** Matches node with any of provided tags **/
    open fun withAnyTag(vararg tag: String): T = addSemanticMatcher(
        tag.map { hasTestTag(it) }.reduce { first, other -> first or other }
    )

    /** Matches node with [imeAction] **/
    open fun withImeAction(imeAction: ImeAction) =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.ImeAction, imeAction))

    /** Matches node with [stateDescription] **/
    open fun withStateDescription(stateDescription: String) =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription,
                stateDescription
            )
        )

    /**
     * 'is' matchers - no arguments
     *  boolean checks - yes or no.
     */

    /** Matches clickable node **/
    open fun isClickable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsActions.OnClick))

    /** Matches not clickable node **/
    open fun isNotClickable() =
        addSemanticMatcher(SemanticsMatcher.keyNotDefined(SemanticsActions.OnClick))

    /** Matches checked node **/
    open fun isChecked() =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ToggleableState,
                ToggleableState.On
            )
        )

    /** Matches not checked node **/
    open fun isNotChecked() =
        addSemanticMatcher(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ToggleableState,
                ToggleableState.Off
            )
        )

    /** Matches checkable node **/
    open fun isCheckable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.ToggleableState))

    /** Matches enabled node **/
    open fun isEnabled() =
        addSemanticMatcher(!SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled))

    /** Matches disabled node **/
    open fun isDisabled() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled))

    /** Matches focusable node **/
    open fun isFocusable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Focused))

    /** Matches not focusable node **/
    open fun isNotFocusable() =
        addSemanticMatcher(SemanticsMatcher.keyNotDefined(SemanticsProperties.Focused))

    /** Matches focused node **/
    open fun isFocused() =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Focused, true))

    /** Matches not focused node **/
    open fun isNotFocused() =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Focused, false))

    /** Matches selected node **/
    open fun isSelected() =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Selected, true))

    /** Matches not selected node **/
    open fun isNotSelected() =
        addSemanticMatcher(SemanticsMatcher.expectValue(SemanticsProperties.Selected, false))

    /** Matches selectable node **/
    open fun isSelectable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected))

    /** Matches scrollable node **/
    open fun isScrollable() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsActions.ScrollBy))

    /** Matches not scrollable node **/
    open fun isNotScrollable() =
        addSemanticMatcher(SemanticsMatcher.keyNotDefined(SemanticsActions.ScrollBy))

    /** Matches heading node **/
    open fun isHeading() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))

    /** Matches dialog node **/
    open fun isDialog() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsDialog))

    /** Matches popup node **/
    open fun isPopup() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsPopup))

    /** Matches node with 'SetText' property */
    open fun isSetText() =
        addSemanticMatcher(SemanticsMatcher.keyIsDefined(SemanticsActions.SetText))

    /** Matches root node **/
    open fun isRoot() = addSemanticMatcher(SemanticsMatcher("isRoot") { it.isRoot })

    /**
     * 'has' matchers deal with relationships between two nodes
     */

    /** Matches node with [ancestor] node **/
    open fun hasAncestor(ancestor: OnNode) =
        addSemanticMatcher(hasAnyAncestor(ancestor.finalMatcher))

    /** Matches node with [parent] node **/
    open fun hasParent(parent: OnNode) =
        addSemanticMatcher(parent.finalMatcher)

    /** Matches node with [child] node **/
    open fun hasChild(child: OnNode) =
        addSemanticMatcher(hasAnyChild(child.finalMatcher))

    /** Matches node with [sibling] node **/
    open fun hasSibling(sibling: OnNode) =
        addSemanticMatcher(hasAnySibling(sibling.finalMatcher))

    /** Matches node with [descendant] node **/
    open fun hasDescendant(descendant: OnNode) =
        addSemanticMatcher(hasAnyDescendant(descendant.finalMatcher))
}
