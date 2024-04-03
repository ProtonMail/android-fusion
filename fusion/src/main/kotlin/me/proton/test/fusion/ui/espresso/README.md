## Fusion Espresso <a name="espresso"></a>
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
