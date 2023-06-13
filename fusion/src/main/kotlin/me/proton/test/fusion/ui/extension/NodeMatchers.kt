package me.proton.test.fusion.ui.extension

import androidx.compose.ui.test.SemanticsMatcher
import me.proton.test.fusion.ui.compose.wrappers.NodeMatchers

@Suppress("UNCHECKED_CAST")
fun <T : NodeMatchers<T>> NodeMatchers<T>.addSemanticsMatchers(vararg matcher: SemanticsMatcher) =
    apply {
        matcher.forEach {
            matchers.add(it)
        }
        return this as T
    }