
# Fusion

***Fusion*** is a lightweight Android test framework that combines ***Espresso***, ***UI Automator*** and ***ComposeUiTest*** into one easy-to-use API with the clear syntax, at the same time keeping the native Android frameworks APIs unchanged.

Multiple reasons to use ***Fusion*** test framework:
1. Seamless API for all native Android test frameworks that reduces boilerplate code to minimum.
2. Syntax that is clear, easy to learn and predictable.
3. Integrated wait functions (work for all APIs: Espresso, UI Automator or ComposeUiTest). No more need to deal with `IdlingResource`. By default, ***Fusion*** explicitly waits for the `View` being visible before proceeding.
4. Pre-defined API to control test device, send shell commands, intents and take screenshots.
5. Pre-created `RetryRule` allows to re-run failed tests.
6. Similar to Fusion iOS test framework (upcoming) - learn one concept for both mobile platforms.

### Fusion project structure:
- [rules](fusion/src/main/kotlin/me/proton/fusion/rules)
  - [RetryRule.kt](fusion/src/main/kotlin/me/proton/fusion/rules/RetryRule.kt) - use this rule to retry failed test runs.
- [ui](fusion/src/main/kotlin/me/proton/fusion/ui) - the core ***Fusion*** package with its API, built on top of ***UiAutomator***, ***Espresso*** and ***ComposeUiTest*** frameworks. 
  - [compose](fusion/src/main/kotlin/me/proton/fusion/ui/compose) - Fusion API for ComposeUiTest.
    - [NodeBuilder.kt](fusion/src/main/kotlin/me/proton/fusion/ui/compose/NodeBuilder.kt) - Provides the API for Node SemanticsMatcher generation.
    - [OnAllNodes.kt](fusion/src/main/kotlin/me/proton/fusion/ui/compose/OnAllNodes.kt) - Contains identifiers, actions, and checks to find all semantics nodes that match the given condition.
    - [OnNodes.kt](fusion/src/main/kotlin/me/proton/fusion/ui/compose/OnNodes.kt) - Contains identifiers, actions, and checks to find a single semantics node that matches the given condition.
  - [device](fusion/src/main/kotlin/me/proton/fusion/ui/device)
    - [OnDevice.kt](fusion/src/main/kotlin/me/proton/fusion/ui/device/OnDevice.kt) - The collection of functions to control the test device using the UiDevice API.
  - [espresso](fusion/src/main/kotlin/me/proton/fusion/ui/espresso) - Fusion Espresso API.
      - [Actions.kt](fusion/src/main/kotlin/me/proton/fusion/ui/espresso/Actions.kt) - Contains the collection of custom ViewActions.
      - [OnIntent.kt](fusion/src/main/kotlin/me/proton/fusion/ui/espresso/OnIntent.kt) - Builder-like class that simplifies Espresso-Intents `intending()` and `intended()` syntax.
      - [OnListView.kt](fusion/src/main/kotlin/me/proton/fusion/ui/espresso/OnListView.kt) - Contains Fusion API to act and assert Views inside the `ListView`.
      - [OnRecyclerView.kt](fusion/src/main/kotlin/me/proton/fusion/ui/espresso/OnRecyclerView.kt) - Contains Fusion API to act and assert `ViewHolder`s or `View`s inside the `RecyclerView`.
      - [OnRootView.kt](fusion/src/main/kotlin/me/proton/fusion/ui/espresso/OnRootView.kt) - Simplifies syntax for applying multiple `RootMatchers` to a root view.
      - [OnView.kt](fusion/src/main/kotlin/me/proton/fusion/ui/espresso/OnView.kt) - Contains `ViewActions` and `ViewAssertions` Fusion API for a single `View`.
      - [OnViewMatchers.kt](fusion/src/main/kotlin/me/proton/fusion/ui/espresso/OnViewMatchers.kt) - Provides the API for `ViewMatchers` generation.
  - [uiautomator](fusion/src/main/kotlin/me/proton/fusion/ui/uiautomator) - Fusion UiAutomator API.
    - [ByObject.kt](fusion/src/main/kotlin/me/proton/fusion/ui/uiautomator/ByObject.kt) - A class that wraps interactions on `UiObject2` element.
    - [ByObjects.kt](fusion/src/main/kotlin/me/proton/fusion/ui/uiautomator/ByObjects.kt) - A class that wraps interactions on `UiObject2` elements.
    - [BySelectorGenerator.kt](fusion/src/main/kotlin/me/proton/fusion/ui/uiautomator/BySelectorGenerator.kt) - Generates selector for `UiObject2` element.
    - [UiSelectorObject.kt](fusion/src/main/kotlin/me/proton/fusion/ui/uiautomator/UiSelectorObject.kt) - A class that wraps interactions on the `UiObject` element.
- [utils](fusion/src/main/kotlin/me/proton/fusion/utils)
  - [ActivityProvider.kt](fusion/src/main/kotlin/me/proton/fusion/utils/ActivityProvider.kt) - Provides top activity from activity stack in `Stage.RESUMED` or in specified stage.
  - [FileUtils.kt](fusion/src/main/kotlin/me/proton/fusion/utils/FileUtils.kt) - Helper class to deal with files and folders used during testing.
  - [MimeTypes.kt](fusion/src/main/kotlin/me/proton/fusion/utils/MimeTypes.kt) - Keeps multiple MIME types to test intents.
  - [Shell.kt](fusion/src/main/kotlin/me/proton/fusion/utils/Shell.kt) - Collection of shell commands to control device under test, send intents, etc.
  - [StringUtils.kt](fusion/src/main/kotlin/me/proton/fusion/utils/StringUtils.kt) - Contains functions that return string resource by its id using target app context.
- [waits](fusion/src/main/kotlin/me/proton/fusion/waits)
  - [Waits.kt](fusion/src/main/kotlin/me/proton/fusion/waits/Waits.kt) - Contains wait functions and custom retry actions.
- [waits](fusion/src/main/kotlin/me/proton/fusion/waits)
- [Fusion.kt](fusion/src/main/kotlin/me/proton/fusion/Fusion.kt) - an entry point to Fusion API. 
- [FusionConfig.kt](fusion/src/main/kotlin/me/proton/fusion/FusionConfig.kt) - config file to control and tweak Fusion behavior.

## Table of Contents
1. [Getting started](#getting_started)
2. [Fusion Espresso API](#espresso)
3. [Fusion UI Automator API](#uiautomator)
4. [Fusion UiDevice API](#device)
5. [Fusion ComposeUiTest API](#composeuitest)

## Getting started <a name="getting_started"></a>
To start using ***Fusion API*** simply extend your test class with `Fusion` interface.
```kotlin
class ProtonTestClass : Fusion {

    @Test
    fun sampleTest() {
      
        // Espresso
        view.withId(R.id.text).inRoot(rootView.isDialog()).typeText("Secure Email")
        listView.onListItem().atPosition(0).click()
        recyclerView.withId(R.id.inbox).onItemAtPosition(0).click()
        intent.hasAction(Intent.ACTION_SEND).toPackage("ch.proton.com").checkSent()
  
        // ComposeUiTest
        node.withTag("testTag").assertTextEquals("Proton Drive")
        allNodes.withTag("testTag").assertEach(node.containsText("@proton.me"))
  
        // UiDevice
        device.pressHome().pressRecentApps()
  
        // UI Automator - UiObject
        uiObject.withPkg("ch.protonmail.android").withText("Proton Mail").click()

        // UI Automator - UiObject2
        byObject.withContentDesc("Search").click()
        byObjects.containsText("@proton.me").atPosition(0).click()
    }
}
```
This is the entry point to Fusion API:
- Espresso:
  - ***intent*** - use it to test intents. Covers `intending()` and `intended()` APIs.
  - ***listView*** - should be used to interact with `ListView` elements. Covers Espresso `onData()` API.
  - ***recyclerView*** - should be used to interact with `RecyclerView` items or views inside the `ViewHolder` item.
  - ***rootView*** - use it to interact with root views.
  - ***view*** - the shortcut to interact with `View` elements. Covers Espresso `onView()` API.
- UI Automator:
  - ***uiObject*** - an alias for `UiObject` UI Automator API.
  - ***byObject*** - an alias for `UiObject2` UI Automator API.
  - ***byObjects*** - an alias for `UiObject2` UI Automator API where you interact with  multiple `UiObject2` objects.
- ComposeUiTest:
  - ***node*** - an alias for `SemanticsNode` ComposeUiTest API.
  - ***allNodes*** - an alias for `SemanticsNode` ComposeUiTest API where you interact with  multiple `SemanticsNode` elements.
- System:
  - ***device*** - collection of functions to control the test device using the UiDevice API.
  - ***shell*** - collection of functions to control device system parameters, and helper functions where system shell is used.
  
## Fusion Espresso API <a name="espresso"></a>
See Android ***Espresso*** documentation [here](https://developer.android.com/training/testing/espresso).

### Operating on single View

Simple usage with one `ViewMatcher`:
```kotlin
view.withId(R.id.addContactItem).click()
```
A bit more complex usage when multiple matchers are used to locate a `View`:
```kotlin
view
    .instanceOf(FloatingActionButton::class.java)
    .withVisibility(ViewMatchers.Visibility.VISIBLE)
    .click()
```

An example of locating `View` based on its child. Similar approach can be taken when locating a `View` based on sibling, descendant, parent, ancestor:
```kotlin
view.withChild(view.withText("OK")).click()
```

Combining multiple actions for a single `View`:
```kotlin
view.withId(R.id.emailEditText).clearText().typeText("proton@pm.me")
```

### Locating `View` inside Root view
Sometimes, when target `View` is located inside a platform pop-up or dialog `RootMatchers` should be provided to point Espresso to locate it in root view hierarchy:
```kotlin
view
    .withId(R.id.contact_name)
    .inRoot(rootView.isDialog())
    .typeText("Proton")

``` 

### Dealing with ListView

Simple `ListView` example:
```kotlin
listView.atPosition(0).click()
```
Operating on a `View` that is located inside the `ListView` item on specified adapter position:
```kotlin
listView
    .atPosition(0)
    .onListItem(view.withId(R.id.star))
    .click()
```
To operate on an item inside the `ListView` provide the item matcher `onListItem(withFolderName("Inbox"))` and if there is more than one `ListView` in the hierarchy specify exactly what adapter should be used: `inAdapter(view.withId(R.id.folders_list_view).matcher())`
```kotlin
listView
    .inAdapter(view.withId(R.id.folders_list_view))
    .onListItem(withFolderName("Inbox"))
    .click()
```
In above example `withFolderName()` is a custom data matcher.

### Dealing with RecycleView

Below example shows how to perform action on `RecyclerView.ViewHolder` item (which represents an item in RecyclerView list) that matches provided `withContactEmail(email)` matcher:
```kotlin
recyclerView
    .withId(R.id.contactsRecyclerView)
    .onHolderItem(withContactEmail(email))
    .click()
```

In the following example you can see how to click the `RecyclerView.ViewHolder` child or descendant view using `onItemChildView()` function that takes as an argument child/descendant view matcher `view.withId(R.id.accUserMoreMenu)`:
```kotlin
recyclerView
    .withId(R.id.accountsRecyclerViewId)
    .onHolderItem(withAccountEmailInAccountManager(email))
    .onItemChildView(view.withId(R.id.accUserMoreMenu))
    .click()
```

### Testing intents

An example of applying multiple intent matchers and checking that matched intent was sent.  
```kotlin
intent
    .hasAction(Intent.ACTION_SEND)
    .toPackage("ch.proton.com")
    .checkSent()
```

Stubbing all external intents. Use this approach to block any intent that targets the 3rd party app in order to not leave the app under test. Usually called from `@Before` or `@BeforeClass` function.
```kotlin
@BeforeClass
fun stubIntents() {
    intent
        .isNotInternal()
        .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
}
```

Release intent(s) after each test case. Usually called from `@After` function. 
```kotlin
@AfterClass
fun releaseIntents() {
    intent.release()
}
```

Stubbing matched intent with prepared in advance activity result.
```kotlin
fun stubMatchedIntent() {
    val data = Intent()
    val phone = "123456789"
    val result = Instrumentation.ActivityResult(Activity.RESULT_OK, data)
    data.putExtra("phone", phone)

    intent.hasExtra("name", "Proton").checkSent()
    intent.toPackage("com.android.contacts").respondWith(result)

    view.withId(R.id.contact).click()
    view.withId(R.id.phone_number).withText(phone).checkIsDisplayed()
}
```

## Fusion UI Automator API <a name="uiautomator"></a>
See Android ***UI Automator*** documentation [here](https://developer.android.com/training/testing/other-components/ui-automator).

Fusion UI Automator API follows the same API split as native Android UI Automator framework - UiObject and UiObject2. UiObject is represented by ***uiObject*** and ***byObject*** represents UiObject2.

###uiObject - uses UiObject API
One matcher case:
```kotlin
uiObject.withText("Proton Mail").click()
```

Clicking element that matched multiple matchers:
```kotlin
uiObject.withPkg("ch.protonmail.android").withText("Proton Mail").click()
```

Controlling element existence timeout before interacting with it (default value is 10 seconds):
```kotlin
uiObject.withText("Proton Mail").withTimeout(15_000L).click()
```

Locating element by index and waiting for enabled state:
```kotlin
uiObject.byIndex(0).waitForEnabled().click()
```

Locating element by index and interacting with its child:
```kotlin
uiObject.byIndex(0).onChild(uiObject.withText("Login")).click()
```

###byObject - uses UiObject2 API
Simple usage:
```kotlin
byObject.withContentDesc("Search").click()
```

Wait for element clickable state:
```kotlin
byObject.withContentDesc("Search").waitForClickable().click()
```

Interacting with descendant elements:
```kotlin
byObject
    .withText("Inbox")
    .hasDescendant(byObject.withResId("me.proton.fusion", "android:id/button1"))
    .click()
```

Searches all elements that contain `"@proton.me"` text and performs a click on first element:
```kotlin
byObjects.containsText("@proton.me").atPosition(0).click()
```


## Fusion UiDevice API <a name="device"></a>
Fusion removes boilerplate code for the ***UiDevice*** API by allowing you to chain device actions one after another.

```kotlin
UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
```
Example:
```kotlin
device.pressHome().pressRecentApps()
```


## Fusion ComposeUiTest API <a name="composeuitest"></a>
See Android ***ComposeUiTest*** documentation [here](https://developer.android.com/jetpack/compose/testing).

In order to use Fusion ComposeUiTest API assign proper compose test rule to `FusionConfig.compose.testRule` variable. Do it before running any compose test.

Use either `createComposeRule()`:
```kotlin
@BeforeClass
fun setUp() {
    FusionConfig.compose.testRule = createComposeRule()
}
```
or `createAndroidComposeRule<YourAppActivity>()`:
```kotlin
@BeforeClass
fun setUp() {
    FusionConfig.compose.testRule = createAndroidComposeRule<MainActivity>()
}
```

### Single node interactions
Fusion API implements node wait mechanism which is triggered before each action or check expression. The default timeout is 10 seconds. It waits for the node existence before proceeding with the action except the `waitUntilGone()` case. The same as in Fusion Espresso and Fusion UI Automator APIs. 

A simple node interaction example:
```kotlin
node.withTag("testTag").click()
```

Combining multiple node matchers:
```kotlin
node.withTag("testTag").withText("sampleText").assertIsDisplayed()
```

Asserting node existence with timeout before checking if it is displayed (uses native `waitUntil()` mechanism):
```kotlin
node.withTag("testTag").withTimeout(10_000L).assertIsDisplayed()
```

The above approach with timeout can be also used with node actions:
```kotlin
node.withTag("testTag").withTimeout(10_000L).clearText().typeText("Secure")
```

Locating the node based on its child:
```kotlin
node.hasChild(node.withTag("childTestTag")).click()
```

Verifying that node does not exist: 
```kotlin
node.withTag("testTag").isChecked().assertDoesNotExist()
```
Waiting for a node that existed before to disappear from the hierarchy:
```kotlin
node.withTag("testTag").isChecked().waitUntilGone()
```

### Interacting with nested nodes or siblings
Acting on the node's child at position:
```kotlin
node.withTag("testTag").onChildAt(0).click()

```
Interacting with the node's child that matches a given matcher:
```kotlin
node.withTag("testTag").onChild(node.withText("Secure Email")).click()
```
Use `onSibling()` to locate sibling node and `onDescendant()` for a deeper look in the hierarchy.

### Interacting with all semantics nodes

Asserting all nodes with test tag contain given text:
```kotlin
allNodes.withTag("testTag").assertEach(node.containsText("@proton.me"))
```

Asserting that at least one node from the list of all nodes matches the given matcher:
```kotlin
allNodes.assertAny(node.withText("Proton Mail"))
```

Interacting with a node from all nodes by position:
```kotlin
allNodes.atPosition(0).click()
```

Interacting with the child of one of the nodes that were filtered by the test tag:
```kotlin
allNodes.withTag("testTag").onChild(node.withText(R.string.settings)).click()
```

Explore the rest yourself to learn more!

## License

The code and data files in this distribution are licensed under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. See <https://www.gnu.org/licenses/> for a copy of this license.

Copyright (c) 2021 Proton Technologies AG
