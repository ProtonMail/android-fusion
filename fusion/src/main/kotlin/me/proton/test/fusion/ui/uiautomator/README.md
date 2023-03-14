## Fusion UI Automator <a name="uiautomator"></a>
See Android **UI Automator** documentation [here](https://developer.android.com/training/testing/other-components/ui-automator).

Fusion UI Automator API follows the same API split as native Android UI Automator framework - UiObject and UiObject2. UiObject is represented by **uiObject** and **byObject** represents UiObject2.

### uiObject - uses UiObject API
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

### byObject - uses UiObject2 API
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

## License

The code and data files in this distribution are licensed under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. See <https://www.gnu.org/licenses/> for a copy of this license.

Copyright (c) 2021 Proton Technologies AG
