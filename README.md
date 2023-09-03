# 🍀Floating Bubble View
An Android library that adds floating bubbles to your home screen 🎨, supports both XML and 💘 Jetpack Compose

<br>


<div align="center">

| 🍀 Bubble 🎨 |  🔥 Custom 💘   | 
| :-: | :-: |
| <img src="https://github.com/TorryDo/assets/blob/main/floating_bubble_view/demo/bubble.gif" height="600" width="272"> | <img src="https://github.com/TorryDo/assets/blob/main/floating_bubble_view/demo/custom_view.gif" height="600" width="276"> |


[<img src="https://img.shields.io/badge/platform-Android-yellow.svg" valign="middle">](https://www.android.com)
[<img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" valign="middle">](https://android-arsenal.com/api?level=21)
[<img src="https://img.shields.io/maven-central/v/io.github.torrydo/floating-bubble-view" valign="middle">]()
[<img src="https://img.shields.io/badge/License-Apache_2.0-blue.svg" valign="middle">](https://www.apache.org/licenses/LICENSE-2.0)

</div>

&nbsp;

## Variants
- ### Flutter version
     If you are looking for a Flutter version of this library, check [dash_bubble](https://github.com/moazelsawaf/dash_bubble), a Flutter plugin that allows you to create a floating bubble on the screen. by [Moaz El-sawaf](https://github.com/moazelsawaf).


## Table of Contents 📝
> 1. [Getting started](#getting_started)  
> 2. [Setup](#setup)
> 3. [Usage](#usage)
> 4. [Contribution guide](#contribution_guide)
> 5. [WIP Note](#note) 🚧
> 6. [License](#license)



## I, Getting started 🍕🍔🍟 <a name="getting_started"/>

<details> <summary> Ensure your app’s minimum SDK version is 21+ and `mavenCentral()` included</summary>
</br>
1. Ensure your app’s minimum SDK version is 21+. This is declared in the module-level `build.gradle` file 

```gradle
android {
    defaultConfig {
        ...
        minSdk 21
    }
```

2. Ensure the `mavenCentral()` repository is declared in the project-level `build.gradle` or `setting.gradle` file:

    <details><summary>build.gradle (project-level)</summary>

    ```gradle
        allprojects {
            repositories {
                mavenCentral()
                ...
            }
            ...
        }
    ```

    </details>


    <details><summary>settings.gradle (alternative step If "allprojects" not found)</summary>

    ```gradle
    pluginManagement {
        repositories {
            ...
            mavenCentral()
        }
    }
    dependencyResolutionManagement {
        ...
        repositories {
            ...
            mavenCentral()
        }
    }
    ```

    </details>

</details>

<!-- <br> -->

<br>


Declare the dependencies in the module-level `build.gradle` file 🍀 <img src="https://img.shields.io/maven-central/v/io.github.torrydo/floating-bubble-view" valign="middle">

```gradle
    dependencies {
        implementation "io.github.torrydo:floating-bubble-view:<LATEST_VERSION>"
    }
```
<br>

## II, Setup 🚀✈🛰 <a name="setup_usage"/>

### 1, extends `FloatingBubbleService` then implements `setupBubble()`  1️⃣ <a name="setup"/>

<details><summary><b>Java version</b></summary>

```java
    public class MyService extends FloatingBubbleService {

        @NonNull
        @Override
        public FloatingBubble.Builder setupBubble(@NonNull FloatingBubble.Action action) {
            return ...;
        }

        // optional, only required if you want to navigate to the expandable-view 
        @Nullable
        @Override
        public ExpandableView.Builder setupExpandableView(@NonNull ExpandableView.Action action) {
            return ...;
        }
    }
```
</details>

<details open><summary><b>Kotlin version</b></summary>

```kotlin
    class MyService: FloatingBubbleService() {

        override fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder {
            return ...
        }

        // optional, only required if you want to navigate to the expandable-view 
        override fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? {
            return ...
        }
    }
```

</details>

</br>

### 2, add bubble-service to manifest file 2️⃣



```xml
<application>
     <!-- these two permissions are added by default -->
     <!-- <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/> -->
     <!-- <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/> -->

     <!-- You can find more permissions, use cases here: https://developer.android.com/about/versions/14/changes/fgs-types-required  -->
     <uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
     
     <service
         android:name="<YOUR_PACKAGE>.MyService"
         android:foregroundServiceType="specialUse"
         >
         <property android:name="android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE" android:value="foo"/>  <!-- optional -->
     </service>

</application>
```

<details><summary>Android 13 and earlier</summary>

```xml
    <!-- these two permissions are added by default -->
    <!-- <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/> -->
    <!-- <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/> -->

    <application>
        ...
        <service android:name="<YOUR_PACKAGE>.MyService" />

    </application>
```
     
</details>


</br>

### 3, start bubble service and enjoy 3️⃣ 🎉🍀
> Make sure "display over other apps" permission is granted, otherwise the app will crash ⚠❗💥

<details><summary><b>Java version</b></summary>

```java
    Intent intent = new Intent(context, MyService.class);
    
    ContextCompat.startForegroundService(this, intent);
    // or
    // startService(intent);           // for android version lower than 8.0 (android O)
    // startForegroundService(intent); // for android 8.0 and higher
```
</details>

<details open><summary><b>Kotlin version</b></summary>

```kotlin
    val intent = Intent(context, MyService::class.java)
    
    ContextCompat.startForegroundService(this, intent)
    // or
    // startService(intent)           // for android version lower than 8.0 (android O)
    // startForegroundService(intent) // for android 8.0 and higher
```

</details>



</br>

## III, Usage 🔥 <a name="usage"/>

### 1, Bubble and ExpandableView

<details><summary>Java</summary>

```java
public class MyService extends FloatingBubbleService {

    @NonNull
    @Override
    public FloatingBubble.Builder setupBubble(@NonNull FloatingBubble.Action action) {

        // You can create your own view manually, or using this helper class that I specially designed for you 💖
        View v = ViewHelper.fromDrawable(this, R.drawable.ic_rounded_blue_diamond, 60, 60);

        v.setOnClickListener{ // sorry I don't remember the syntax :(
            action.navigateToExpandableView();
        }

        return new FloatingBubble.Builder(this)

                // pass view
                .bubble(v)

                // set style for bubble, fade animation by default
                .bubbleStyle(null)

                // set start location for the bubble, (x=0, y=0) is the top-left
                .startLocation(100, 100)        // in dp
                .startLocationPx(100, 100)      // in px

                // enable auto animate bubble to the left/right side when release, true by default
                .enableAnimateToEdge(true)

                // set close-bubble icon attributes, currently only drawable and bitmap are supported
                .closeBubble(R.drawable.ic_close_bubble, 60, 60)

                // set style for close-bubble, null by default
                .closeBubbleStyle(null)

                // show close-bubble, true by default
                .enableCloseBubble(true)

                // the more value (dp), the larger closable-area
                .distanceToClose(100)

                // choose behavior of the bubbles
                // DYNAMIC_CLOSE_BUBBLE: close-bubble moving based on the bubble's location
                // FIXED_CLOSE_BUBBLE: bubble will automatically move to the close-bubble when it reaches the closable-area
                .behavior(BubbleBehavior.DYNAMIC_CLOSE_BUBBLE)

                // enable bottom background, false by default
                .bottomBackground(false)

                // add listener for the bubble
                .addFloatingBubbleListener(new FloatingBubble.Listener() {

                    @Override
                    public void onMove(float x, float y) {} // The location of the finger on the screen which triggers the movement of the bubble.

                    @Override
                    public void onUp(float x, float y) {} // ..., when finger release from bubble

                    @Override
                    public void onDown(float x, float y) {} // ..., when finger tap the bubble
                })

                // set bubble's opacity
                .opacity(1f);
    }

    @Nullable
    @Override
    public ExpandableView.Builder setupExpandableView(@NonNull ExpandableView.Action action) {
        
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_view_test, null);

        layout.findViewById(R.id.card_view).setOnClickListener(v -> {
            Toast.makeText(this, "hello from card view from java", Toast.LENGTH_SHORT).show();
            action.popToBubble();
        });

        return new ExpandableView.Builder(this)
                
                // set view to expandable-view. Jetpack Compose is not available in Java
                .view(layout)

                // set the amount of dimming below the view.
                .dimAmount(0.8f)

                // apply style for the expandable-view
                .expandableViewStyle(null)
                
                // add listener for the expandable-view
                .addExpandableViewListener(new ExpandableView.Listener() {
                    @Override
                    public void onOpenExpandableView() {}

                    @Override
                    public void onCloseExpandableView() {}
                });
    }
}
```
</details>

<details open><summary>Kotlin</summary>

```kotlin
class MyService : FloatingBubbleService() {

    override fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder {

        // You can create your own view manually, or using this helper class that I specially designed for you 💖
        val v = ViewHelper.fromDrawable(this, R.drawable.ic_rounded_blue_diamond, 60, 60);

        v.setOnClickListener{ 
            action.navigateToExpandableView()
        }

        return FloatingBubble.Builder(this)

            // pass view
            .bubble(v)

            // or our sweetie, Jetpack Compose
            .bubble{
                BubbleCompose()
            }

            // set style for the bubble, fade animation by default
            .bubbleStyle(null)

            // set start location for the bubble, (x=0, y=0) is the top-left
            .startLocation(100, 100)        // in dp
            .startLocationPx(100, 100)      // in px

            // enable auto animate bubble to the left/right side when release, true by default
            .enableAnimateToEdge(true)

            // set close-bubble icon attributes, currently only drawable and bitmap are supported
            .closeBubble(R.drawable.ic_close_bubble, 60, 60)

            // set style for close-bubble, null by default
            .closeBubbleStyle(null)

            // show close-bubble, true by default
            .enableCloseBubble(true)

            // the more value (dp), the larger closeable-area
            .distanceToClose(100)

            // choose behavior of the bubbles
            // DYNAMIC_CLOSE_BUBBLE: close-bubble moving based on the bubble's location
            // FIXED_CLOSE_BUBBLE: bubble will automatically move to the close-bubble when it reaches the closable-area
            .behavior(BubbleBehavior.DYNAMIC_CLOSE_BUBBLE)

            // enable bottom background, false by default
            .bottomBackground(false)

            // add listener for the bubble
            .addFloatingBubbleListener(object : FloatingBubble.Listener {
                override fun onMove(x: Float, y: Float) {} // The location of the finger on the screen which triggers the movement of the bubble.
                override fun onUp(x: Float, y: Float) {}   // ..., when finger release from bubble
                override fun onDown(x: Float, y: Float) {} // ..., when finger tap the bubble
            })
            // set bubble's opacity
            .opacity(1f)

    }

    override fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? {

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.layout_view_test, null)

        layout.findViewById<View>(R.id.card_view).setOnClickListener { v: View? ->
            Toast.makeText(this, "hello from kotlin", Toast.LENGTH_SHORT).show();
            action.popToBubble()
        }

        return ExpandableView.Builder(this)

            // You should not pass both view and compose. Passing both will cause crashing. ❗💥
            // set view to expandable-view
            .view(layout)

            // You should not pass both view and compose. Passing both will cause crashing. ❗💥
            // set composable to expandable-view
            .compose {
                TestComposeView(
                    popBack = {
                        action.popToBubble()
                    }
                )
            }

            // set the amount of dimming below the view.
            .dimAmount(0.8f)

            // apply style for the expandable-view
            .expandableViewStyle(null)

            // ddd listener for the expandable-view
            .addExpandableViewListener(object : ExpandableView.Listener {
                override fun onOpenExpandableView() {}
                override fun onCloseExpandableView() {}
            })
    }
}
```

</details>

### 2, Notification
<details><summary>Java</summary>

```java
public class MyService extends FloatingBubbleService {
    
    ...

    // config the initial notification for Bubble on Android 8 and up.
    // return null if you want to show the notification later.
    @Override
    public Notification initialNotification() {
        return new NotificationCompat.Builder(this, channelId())
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
                .setContentTitle("bubble is running")
                .setContentText("click to do nothing")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
    }

    @NonNull
    @Override
    public String channelId() {
        return "you_channel_id";
    }

    @NonNull
    @Override
    public String channelName() {
        return "your channel name";
    }

    @Override
    public int notificationId() {
        return 69;
    }
    ...
}
```

</details>

<details open><summary>Kotlin</summary>

```kotlin
class MyService : FloatingBubbleService() {

    // config the initial notification for Bubble on Android 8 and up.
    // return null if you want to show the notification later.
    open fun initialNotification(): Notification? {
        return NotificationCompat.Builder(this, channelId())
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
            .setContentTitle("bubble is running")
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSilent(true)
            .build()
    }

    override fun channelId() = "your_channel_id"
    override fun channelName() = "your_channel_name"
    override fun notificationId() = 69
}
```

</details>

<details><summary>Notice since Android 13 ⚠ </summary>
<br/>

Starting in Android 13 (API level 33), notifications are only visible if the "POST_NOTIFICATIONS" permission is granted.<br/>

> The service will run normally even if the notification is not visible. 🍀

> P/s: You still need to initialize the notification before showing any view.

</details>

### 3, Check if bubble is running

```java
    Boolean b = FloatingBubbleService.isRunning();
```

### 4, Choosing the Initial View for the Bubble Service

```java
public class MyService extends FloatingBubbleService {
    ...

    @NonNull
    @Override
    public Route initialRoute() { // "Route.Bubble" by default
        return Route.Empty;      
        // Route.Empty: create the service without any view
        // Route.Bubble: create the service with the bubble
        // Route.ExpandableView: create the service with the expandable-view
    }
    ...
}
```


### 5, Detect key press on expandable-view
<details><summary>XML version</summary>

```kotlin
    override fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? {
        ...
        val wrapper: ViewGroup = object : FrameLayout(this) {
            override fun dispatchKeyEvent(event: KeyEvent): Boolean {
                if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                    action.popToBubble()
                    return true
                }
                return super.dispatchKeyEvent(event)
            }
        }

        val layout = inflater.inflate(R.layout.layout_view_test, wrapper) // pass "wrapper" here
        ...
    }
```

</details>
<details open><summary>Jetpack Compose version</summary>

```kotlin
@Composable
fun SomeComposable(){
    ...
    OverrideDispatchKeyEvent { event ->
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            // your code here
        }
        null
    }
}
```
</details>


### 6, Methods in Bubble Service

| Name | Description |
| :- | :- |
| `currentRoute()` | Returns the current route of the service (Empty, Bubble, ExpandableView) |
| `showBubbles()` | Displays the the bubbles |
| `removeBubbles()` | Removes the floating bubble |
| `showExpandableView()` | Displays the expandable-view |
| `removeExpandableView()` | Removes the expandable-view |
| `removeAllViews()` | Removes all views |
| `notify(Notification)` | Displays or updates notification |

<!-- | `updateNotification()`| Updates the displayed notification by calling (again) `setupNotificationBuilder()` | -->

</br>

## IV, Contribution Guide 👏  <a name="contribution_guide">

Contributions are welcome! 🙌

- If you come across a bug or have an idea for a new feature, please let us know by creating an [Issue](https://github.com/TorryDo/Floating-Bubble-View/issues) 🐛💡
- If you've already fixed a bug or implemented a feature, feel free to submit a [Pull request](https://github.com/TorryDo/Floating-Bubble-View/pulls) 🚀
- Having questions, ideas, or feedback? Don't worry, I gotchu. Simply open a [Discussion](https://github.com/TorryDo/Floating-Bubble-View/discussions) 🔊
- Find this project useful? Don't forget to show some love by giving a star ⭐

Thank you! 💖

<br>

## V, Work in Progress 🚧 <a name="note">
This library is still under heavy development. There is still a lot of code cleanup to do, so expect breaking API changes over time.

Please refer to the following page to check out the change-log: [Releases](https://github.com/TorryDo/Floating-Bubble-View/releases)

Everything's gonna be ok! 🍀

<br>

## VI, License <a name="license"/>

```

    Copyright 2022 TorryDo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

```
