package me.proton.fusion.ui.api

import androidx.compose.ui.text.input.ImeAction
import me.proton.fusion.ui.compose.OnNode

interface FusionMatchers<T> {
    val matchers get() = ArrayList<T>()
    val ArrayList<T>.final: T

    fun withText(text: String, exactly: Boolean)
    fun withText(textId: Int, exactly: Boolean)
    fun withContentDescription(contentDescription: String, exactly: Boolean)
    fun withContentDescription(textId: Int, exactly: Boolean)
    fun withTag(tag: String)

    fun isClickable(): T
    fun isNotClickable()
    fun isChecked()
    fun isNotChecked()
    fun isCheckable()
    fun isEnabled()
    fun isDisabled()
    fun isFocusable()
    fun isNotFocusable()
    fun isFocused()
    fun isNotFocused()
    fun isSelected()
    fun isNotSelected()
    fun isSelectable()
    fun isScrollable()
    fun isNotScrollable()
    fun isHeading()
    fun isDialog()
    fun isPopup()
    fun isRoot()

    fun hasStateDescription(stateDescription: String)
    fun hasImeAction(imeAction: ImeAction)
    fun hasSetTextAction()
    fun hasAncestor(ancestor: OnNode)
    fun hasParent(parent: OnNode)
    fun hasChild(child: OnNode)
    fun hasSibling(sibling: OnNode)
    fun hasDescendant(descendant: OnNode)

    @Suppress("UNCHECKED_CAST")
    fun addSemanticMatcher(matcher: T)
}