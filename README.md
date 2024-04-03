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
androidTestImplementation "me.proton.test:fusion:0.9.97"

// build.gradle.kts
androidTestImplementation("me.proton.test:fusion:0.9.97")
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
}
```

Full Fusion Espresso [documentation](./fusion/src/main/kotlin/me/proton/test/fusion/ui/espresso/README.md).


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

Full Fusion Compose [documentation](./fusion/src/main/kotlin/me/proton/test/fusion/ui/compose/README.md).

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

More about Fusion capabilities for UIDevice device - [documentation](./fusion/src/main/kotlin/me/proton/test/fusion/ui/device/README.md).

More about Fusion for UIAutomator - [documentation](./fusion/src/main/kotlin/me/proton/test/fusion/ui/uiautomator/README.md).

### Fusion API entry point

This is the entry point to [Fusion API](./fusion/src/main/kotlin/me/proton/test/fusion/Fusion.kt):

- [Espresso](./fusion/src/main/kotlin/me/proton/test/fusion/ui/espresso/README.md):
    - `intent` - use it to test intents. Covers `intending()` and `intended()` APIs.
    - `listView` - should be used to interact with `ListView` elements. Covers Espresso `onData()`
      API.
    - `recyclerView` - should be used to interact with `RecyclerView` items or views inside
      the `ViewHolder` item.
    - `rootView` - use it to interact with root views.
    - `view` - the shortcut to interact with `View` elements. Covers Espresso `onView()` API.
- [UIAutomator](./fusion/src/main/kotlin/me/proton/test/fusion/ui/uiautomator/README.md):
    - `uiObject` - an alias for `UiObject` UI Automator API.
    - `byObject` - an alias for `UiObject2` UI Automator API.
    - `byObjects` - an alias for `UiObject2` UI Automator API where you interact with
      multiple `UiObject2` objects.
- [ComposeUiTest](./fusion/src/main/kotlin/me/proton/test/fusion/ui/compose/README.md):
    - `node` - an alias for `SemanticsNodeInteraction` ComposeUiTest API.
    - `allNodes` - an alias for `SemanticsNodeInteractionCollection` ComposeUiTest API where you
      interact with
      multiple `SemanticsNodeInteraction` elements.
- [Device](./fusion/src/main/kotlin/me/proton/test/fusion/ui/device/README.md):
    - `device` - collection of functions to control the test device using the UiDevice API.

### Fusion configuration

[FusionConfig.kt](./fusion/src/main/kotlin/me/proton/test/fusion/FusionConfig.kt) file contains configuration parameters to control multiple Fusion parameters.

#### Compose

1. `useUnmergedTree` - controls whether the Compose UI Test framework should use an unmerged tree for UI tests. An unmerged tree could refer to a more detailed or raw view hierarchy, which might be useful for certain types of tests where the default merged or simplified view hierarchy isn't sufficient. Default is `false`.
2. `testRule` - allows dynamically changing the test rule in a thread-safe manner during test execution.
3. `shouldPrintHierarchyOnFailure` - instructs the testing framework to print or log the UI hierarchy in the event of a test failure. This can be invaluable for debugging, providing immediate insight into the UI state at the time of failure.

#### UiAutomator

1. **Search flags** - `shouldSearchByObjectEachAction` and `shouldSearchUiObjectEachAction` flags, both initialized to false, are used to control how UI elements (objects) are located, specifically in the context of using UI selectors or identifiers to interact with elements.

    Both flags offer a way to balance between test robustness and execution speed. By default, with both flags set to false, the system assumes elements do not need to be re-located for each action, optimizing for speed.

2. **Boost function** - `boost(idleTimeout: Long = 0L, selectorTimeout: Long = 0L, actionTimeout: Long = 50L)` is a utility designed specifically for enhancing the performance of UI tests automated with UI Automator in Android test automation projects. It aims to significantly reduce the execution time of UI tests by adjusting key timeouts in the UI Automator's Configurator.

#### Timeout Management
Fusion offers a set of default timeouts for various operations within the test execution process. These include:

- `waitTimeout`: Maximum time to wait for conditions to be met before proceeding.
- `assertTimeout`: Time to wait before asserting a condition to ensure stability.
- `watchInterval`: Frequency at which conditions or changes are checked.

These timeouts are encapsulated within AtomicReference objects to ensure thread-safe updates and retrievals, allowing dynamic adjustments during runtime.

#### Execution Hooks

Defines hooks for key moments in the test lifecycle, which can be customized per test needs. These hooks include:

- `before`: Executed before each test case.
- `after`: Executed after each test case.
- `onFailure`: Invoked when a test case fails.
- `onSuccess`: Invoked when a test case succeeds.

These hooks provide a flexible mechanism to integrate setup, teardown, and custom behavior based on test outcomes.
