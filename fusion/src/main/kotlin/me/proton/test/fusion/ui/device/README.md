## Fusion UiDevice <a name="device"></a>
Fusion removes boilerplate code for the **UiDevice** API by allowing you to chain device actions one after another.

```kotlin
UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
```
Example:
```kotlin
device.pressHome().pressRecentApps()
```
