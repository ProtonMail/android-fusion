## Fusion UiDevice <a name="device"></a>
Fusion removes boilerplate code for the **UiDevice** API by allowing you to chain device actions one after another.

```kotlin
UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
```
Example:
```kotlin
device.pressHome().pressRecentApps()
```

## License

The code and data files in this distribution are licensed under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. See <https://www.gnu.org/licenses/> for a copy of this license.

Copyright (c) 2021 Proton Technologies AG