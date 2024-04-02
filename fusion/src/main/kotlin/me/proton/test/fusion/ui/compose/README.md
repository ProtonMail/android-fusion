## Fusion ComposeUiTest <a name="composeuitest"></a>
See Android **ComposeUiTest** documentation [here](https://developer.android.com/jetpack/compose/testing).

In order to use Fusion Compose make your test class implement FusionComposeTest abstract class.

By default a simple compose rule be  created under the hood using `createComposeRule()`
```kotlin
import androidx.compose.ui.test.junit4.createComposeRule

class FusionComposeTests : FusionComposeTest() {
    @Test
    fun testSimpleText() {
        withContent { // implemented in FusionComposeTest
            Text(text = "test")
        }

        node.withText("test").assertIsDisplayed()
    }
}
```
In case you need to test a specific activity you can override composeRule with
`createAndroidComposeRule<YourAppActivity>()` (or any other rule) for your activity under test:
```kotlin
import androidx.compose.ui.test.junit4.createAndroidComposeRule

class FusionComposeTests : FusionComposeTest() {
    @get:Rule
    override val composeRule: ComposeContentTestRule = 
        createAndroidComposeRule<MainActivity>()

    @Test
    fun activityRule() {
        node.withText(R.string.text).assertIsDisplayed()
    }   
}
```
### Configuration
Available Fusion Compose settings:
```kotlin
FusionConfig.Compose.testRule                       // Global compose rule used by Fusion
FusionConfig.Compose.useUnmergedTree                // Compose 'useUnmergedTree' setting (see Compose docs)
FusionConfig.Compose.shouldPrintHierarchyOnFailure  // Print compose hierarchy on test failure
FusionConfig.Compose.shouldPrintToLog               // Print compose hierarchy on every successful action
FusionConfig.Compose.waitTimeout                    // Wait timeout duration in wait mechanism (default - 10 seconds)
FusionConfig.Compose.watchInterval                  // time interval between retry in wait mechanism (default - 0 seconds)

// action hooks. Triggered on every action and awaited assertions
FusionConfig.Compose.before
FusionConfig.Compose.after
FusionConfig.Compose.onFailure 
FusionConfig.Compose.onSuccess

// example
class BaseTest: FusionComposeTest {
    init {
        FusionConfig.Compose.useUnmergedTree.set(true)
        FusionConfig.Compose.shouldPrintHierarchyOnFailure.set(true)
        FusionConfig.Compose.shouldPrintToLog.set(false)
        FusionConfig.Compose.waitTimeout.set(1000.milliseconds)
        FusionConfig.Compose.watchInterval.set(50.milliseconds)

        FusionConfig.Compose.onFailure = { takeScreenshot(getTestName()) }
        FusionConfig.Compose.after = { sendData() }
    }
}
```

### Wait mechanism

Every compose action (click, swipe, type text, etc.) goes through wait mechanism which tries to execute the action and keeps retrying until the action is either successful or timeout is reached. Default timeout is 10 seconds. 

Note that all actions are awaited by default, but assertions - are not. In order to await for assertion you can use `await()` function. Timeout and watch interval can be set in config

```kotlin
node
    .withText("test")
    .assertExists()     // will be executed immediately
    .click()            // will be waited for automatically
    .await {            // all asserts in await() block will be waited for
        assertIsDisplayed()
        assertIsEnabled()
    }
```

### Extending builders

If there is a missing assertion, selector or action for your purposes you can extend Compose builders:
```kotlin
// Custom matcher
fun NodeMatchers<OnNode>.customMatcher(): OnNode {
    val matcher = SemanticsMatcher("description") { ... }
    return addSemanticMatcher(matcher)
}

// Custom action
fun NodeActions.customAction() = waitFor {
    interaction
        .performSemanticsAction { ... }
}

// Custom assertion
fun NodeAssertions.customAssertion() = apply {
    interaction
        .fetchSemanticsNode("could not fetch semantics node")
        .run {
            // Do something with semantics node
        }
}

// Use them in tests
node
    .customMatcher()
    .customAction()
    .customAssertion()
```

### Single node interactions
A simple node interaction example:
```kotlin
node.withTag("testTag").click()
```

Combining multiple node matchers:
```kotlin
node.withTag("testTag").withText("sampleText").isEnabled().assertIsDisplayed()
```

Locating the node based on its child:
```kotlin
node.hasChild(node.withTag("childTestTag")).click()
```

Verifying that node does not exist: 
```kotlin
node.withTag("testTag").isChecked().assertDoesNotExist()
```

### Interacting with nested nodes or siblings
Acting on the node's child at position:
```kotlin
node.withTag("testTag").onChildAt(0).click()
```
Interacting with the node's child that matches a given matcher:
```kotlin
node.withTag("testTag").onChild(node.withText("test")).click()
```
Use `onSibling()` to locate sibling node and `onDescendant()` for a deeper look in the hierarchy.

### Interacting with all semantics nodes

Asserting all nodes with test tag contain given text:
```kotlin
allNodes.withTag("testTag").assertEach(node.containsText("test"))
```

Asserting that at least one node from the list of all nodes matches the given matcher:
```kotlin
allNodes.assertAny(node.withText("test"))
```

Interacting with a node from all nodes by position:
```kotlin
allNodes.atPosition(0).click()
```

Interacting with the child of one of the nodes that were filtered by the test tag:
```kotlin
allNodes.withTag("testTag").onChild(node.withText(R.string.settings)).click()
```
