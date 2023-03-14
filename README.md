# Fusion

**Fusion** is a extensible lightweight 
Android [instrumented](https://developer.android.com/training/testing/instrumented-tests) 
test framework that combines
**Espresso**, **UI Automator** and **ComposeUiTest** into one easy-to-use API with the clear syntax,
at the same time keeping the native Android frameworks APIs unchanged.

## Getting started

Add Fusion dependency to your build file:

```kotlin
repositories {
    mavenCentral()
}

// build.gradle
androidTestImplementation "me.proton.test:fusion:0.9.50"

// build.gradle.kts
androidTestImplementation("me.proton.test:fusion:0.9.50")
```

To start using **Fusion API** simply import required builder

### Espresso

```kotlin
import me.proton.test.fusion.Fusion.view
import me.proton.test.fusion.Fusion.listView
import me.proton.test.fusion.Fusion.recyclerView

class EspressoTests {

    @Test
    fun onViewTest() {
        view
            .withId(R.id.text)
            .withRootMatcher(isDialogPopup())
            .typeText("Secure Email")
    }

    @Test
    fun onListViewTest() {
        listView
            .onListItem()
            .atPosition(0)
            .click()
    }

    @Test
    fun onRecyclerViewTest() {
        recyclerView
            .withId(R.id.inbox)
            .onItemAtPosition(0)
            .click()

        view
            .withText(R.string.test)
            .checkIsDisplayed()
    }

```

### Compose UI

  ```kotlin
import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.Fusion.allNodes

class ComposeUITests : FusionComposeTest {

    @Test
    fun singleNodeTest() {
        node
            .withTag("testTag")
            .click()

        node
            .withText("text")
            .assertExists()
            .await {
                assertIsDisplayed()
            }
    }

    @Test
    fun nodeCollectionTest() {
        allNodes
            .withTag("testTag")
            .assertEach(
                node.containsText("test")
            )

        allNodes
            .withText("Item")
            .onFirst()
            .click()
            .await {
                assertDoesNotExist()
            }
    }
}
```

### Device and UI Automator

```kotlin
import me.proton.test.fusion.Fusion.device
import me.proton.test.fusion.Fusion.uiObject
import me.proton.test.fusion.Fusion.byObject

class InstrumentationTests {

    @Test
    fun deviceTest() {
        device
            .pressHome()
            .pressRecentApps()
    }

    @Test
    fun uiObjectTest() {
        uiObject
            .withPkg("me.package.example")
            .withText("Example")
            .click()
    }

    @Test
    fun byObjectTest() {
        byObject
            .withContentDesc("Search")
            .click()

        byObjects
            .containsText("test")
            .atPosition(0)
            .click()
    }
}
```

This is the entry point to Fusion API:

- Espresso:
    - `intent` - use it to test intents. Covers `intending()` and `intended()` APIs.
    - `listView` - should be used to interact with `ListView` elements. Covers Espresso `onData()`
      API.
    - `recyclerView` - should be used to interact with `RecyclerView` items or views inside
      the `ViewHolder` item.
    - `rootView` - use it to interact with root views.
    - `view` - the shortcut to interact with `View` elements. Covers Espresso `onView()` API.
- UI Automator:
    - `uiObject` - an alias for `UiObject` UI Automator API.
    - `byObject` - an alias for `UiObject2` UI Automator API.
    - `byObjects` - an alias for `UiObject2` UI Automator API where you interact with
      multiple `UiObject2` objects.
- ComposeUiTest:
    - `node` - an alias for `SemanticsNodeInteraction` ComposeUiTest API.
    - `allNodes` - an alias for `SemanticsNodeInteractionCollection` ComposeUiTest API where you
      interact with
      multiple `SemanticsNodeInteraction` elements.
- System:
    - `device` - collection of functions to control the test device using the UiDevice API.

## License

The code and data files in this distribution are licensed under the terms of the GNU General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your
option) any later version. See <https://www.gnu.org/licenses/> for a copy of this license.

Copyright (c) 2021 Proton Technologies AG
