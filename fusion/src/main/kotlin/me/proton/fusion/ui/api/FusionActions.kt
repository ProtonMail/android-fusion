package me.proton.fusion.ui.api

import me.proton.fusion.ui.api.enums.SwipeDirection

interface FusionActions<T> {
    val interaction: T

    /** actions **/
    fun click(): FusionActions<T>
    fun scrollTo(): FusionActions<T>
    fun swipe(direction: SwipeDirection): FusionActions<T>
    fun clearText(): FusionActions<T>
    fun typeText(text: String): FusionActions<T>
    fun replaceText(text: String): FusionActions<T>
    fun performImeAction(): FusionActions<T>
}