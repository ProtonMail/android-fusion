/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 by Proton Technologies A.G. (Switzerland) Email: contact@protonmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package me.proton.test.fusion.ui.espresso.builders

import android.app.Activity
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.test.espresso.intent.ActivityResultFunction
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.FusionConfig.targetContext
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
import kotlin.time.Duration

/**
 * Builder like class that simplifies [intending] and [intended] syntax.
 */
class OnIntent {

    private val matchers = mutableListOf<Matcher<Intent>>()
    private var defaultTimeout: Duration = FusionConfig.Intents.waitTimeout.get()

    fun withTimeout(duration: Duration) = apply { defaultTimeout = duration }

    fun init() = apply { Intents.init() }

    fun release() = apply { Intents.release() }

    fun anyIntent() = apply {
        matchers.add(IntentMatchers.anyIntent())
    }

    fun isInternal() = apply {
        matchers.add(IntentMatchers.isInternal())
    }

    fun isNotInternal() = apply {
        matchers.add(CoreMatchers.not(IntentMatchers.isInternal()))
    }

    fun filterEquals(filterIntent: Intent) = apply {
        matchers.add(IntentMatchers.filterEquals(filterIntent))
    }

    /** See [Intent] for the list of actions. **/
    fun hasAction(action: String) = apply {
        matchers.add(IntentMatchers.hasAction(action))
    }

    fun hasAction(actionMatcher: Matcher<String>) = apply {
        matchers.add(IntentMatchers.hasAction(actionMatcher))
    }

    fun hasCategories(categories: Set<String>) = apply {
        matchers.add(IntentMatchers.hasCategories(categories))
    }

    fun hasComponent(component: String) = apply {
        this.matchers.add(IntentMatchers.hasComponent(component))
    }

    fun hasComponent(componentName: ComponentName) = apply {
        matchers.add(IntentMatchers.hasComponent(componentName))
    }

    fun hasComponent(componentNameMatcher: Matcher<ComponentName>) = apply {
        matchers.add(IntentMatchers.hasComponent(componentNameMatcher))
    }

    fun hasData(data: String) = apply {
        matchers.add(IntentMatchers.hasData(data))
    }

    fun hasData(@StringRes dataRes: Int) = apply {
        matchers.add(IntentMatchers.hasData(targetContext.resources.getString(dataRes)))
    }

    fun hasDataUri(dataUri: Uri) = apply {
        matchers.add(IntentMatchers.hasData(dataUri))
    }

    fun hasDataUriMatcher(dataUriMatcher: Matcher<Uri>) = apply {
        matchers.add(IntentMatchers.hasData(dataUriMatcher))
    }

    fun hasDataString(dataString: Matcher<String>) = apply {
        matchers.add(IntentMatchers.hasDataString(dataString))
    }

    fun hasExtra(extraKey: String, extraValue: Any) = apply {
        matchers.add(IntentMatchers.hasExtra(extraKey, extraValue))
    }

    fun hasExtra(extraKeyMatcher: Matcher<String>, extraValueMatcher: Matcher<Any>) = apply {
        matchers.add(IntentMatchers.hasExtra(extraKeyMatcher, extraValueMatcher))
    }

    fun hasExtras(extraMatcherBundle: Matcher<Bundle>) = apply {
        matchers.add(IntentMatchers.hasExtras(extraMatcherBundle))
    }

    fun hasExtraWithKey(extraWithKey: String) = apply {
        matchers.add(IntentMatchers.hasExtraWithKey(extraWithKey))
    }

    fun hasExtraWithKey(extraWithKeyMatcher: Matcher<String>) = apply {
        matchers.add(IntentMatchers.hasExtraWithKey(extraWithKeyMatcher))
    }

    fun hasFlag(flag: Int) = apply {
        matchers.add(IntentMatchers.hasFlag(flag))
    }

    fun hasFlags(flags: Int) = apply {
        matchers.add(IntentMatchers.hasFlags(flags))
    }

    fun hasPackage(packageName: String) = apply {
        matchers.add(IntentMatchers.hasPackage(packageName))
    }

    fun hasPackage(packageNameMatcher: Matcher<String>) = apply {
        matchers.add(IntentMatchers.hasPackage(packageNameMatcher))
    }

    fun hasType(type: String) = apply {
        matchers.add(IntentMatchers.hasType(type))
    }

    fun hasType(typeMatcher: Matcher<String>) = apply {
        matchers.add(IntentMatchers.hasType(typeMatcher))
    }

    fun toPackage(toPackage: String) = apply {
        matchers.add(IntentMatchers.toPackage(toPackage))
    }

    // Checks with wait that intent with given matchers is sent
    fun checkSent() {
        intended(intentMatcher())
    }

    fun respondWith(result: Instrumentation.ActivityResult) {
        intending(intentMatcher()).respondWith(result)
    }

    fun respondWithFunction(resultFunction: ActivityResultFunction) {
        intending(intentMatcher()).respondWithFunction(resultFunction)
    }

    fun stubExternalIntents() {
        intending(CoreMatchers.not(IntentMatchers.isInternal()))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    fun checkBrowserOpened(url: String) {
        intended(
            AllOf.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(url)
            )
        )
    }

    private fun intentMatcher() = AllOf.allOf(matchers)
}
