# 🍀Floating Bubble View
An Android library that adds floating bubbles to your home screen 🎨, supporting both XML 💘 Jetpack Compose

&nbsp;

https://user-images.githubusercontent.com/85553681/223082521-789146d2-c8f7-4e54-a4d7-f281cd495404.mp4


&nbsp;



| Platform |  API  | Version | License |
| :-: | :-: | :-: | :-: | 
|[<img src="https://img.shields.io/badge/platform-Android-yellow.svg" valign="middle">](https://www.android.com)| [<img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" valign="middle">](https://android-arsenal.com/api?level=21) | [<img src="https://img.shields.io/maven-central/v/io.github.torrydo/floating-bubble-view" valign="middle">]() | [<img src="https://img.shields.io/badge/License-Apache_2.0-blue.svg" valign="middle">](https://www.apache.org/licenses/LICENSE-2.0) |

</br>

## Variants
- ### Flutter version
     If you are looking for a Flutter version of this library, check [dash_bubble](https://github.com/moazelsawaf/dash_bubble), a Flutter plugin that allows you to create a floating bubble on the screen. by [Moaz El-sawaf](https://github.com/moazelsawaf).

## Table of Contents 📝
1. [Getting started](#getting_started)  
2. [Setup](#setup)
3. [Usage](#usage)
4. [Migrate](#migrate)
5. [Note](#note)
6. [License](#license)


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


    <details><summary>settings.gradle (alternative step If "allprojects" not found in the above step)</summary>

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

</br>

Declare the dependencies in the module-level `build.gradle` file

```gradle
    dependencies {
        implementation("io.github.torrydo:floating-bubble-view:<LATEST_VERSION>")
    }
```
</br>

## II, Setup & Usage 🚀✈🛰 <a name="setup_usage"/>

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
    <!-- these two permissions are added by default -->
    <!-- <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/> -->
    <!-- <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/> -->

    <application>
        ...
        <service android:name="<YOUR_PACKAGE>.MyService" />

    </application>
```

</br>

### 3, start bubble service and enjoy 3️⃣ 🎉🍀
> Make sure "display over other apps" permission is granted, otherwise the app will crash ⚠

<details><summary><b>Java version</b></summary>

```java
    Intent intent = new Intent(context, MyService.class);

    startService(intent);           // for android version lower than 8 (android O)
    startForegroundService(intent); // for android 8 and higher
```
</details>

<details open><summary><b>Kotlin version</b></summary>

```kotlin
    val intent = Intent(context, MyService::class.java)

    startService(intent)           // for android version lower than 8 (android O)
    startForegroundService(intent) // for android 8 and higher
```

</details>



</br>

## III, Usage 🔥 <a name="usage"/>

<details><summary><b>Java version</b></summary>

```java
public class MyService extends FloatingBubbleService {

    String data = null;

    /**
     * Sets up a notification for Bubble on Android 8 and up.
     *
     * @param channelId The ID of the notification channel.
     * @return The notification instance.
     */
    @NonNull
    @Override
    public Notification setupNotificationBuilder(@NonNull String channelId) {
        return new NotificationCompat.Builder(this, channelId)
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
    public FloatingBubble.Builder setupBubble(@NonNull FloatingBubble.Action action) {

        Log.d("<>", "data = " + data);

        return new FloatingBubble.Builder(this)
                // set bubble icon attributes, currently only drawable and bitmap are supported
                .bubble(R.drawable.ic_rounded_blue_diamond, 60, 60)

                // set style for bubble, fade animation by default
                .bubbleStyle(null)

                // set start location of bubble, (x=0, y=0) is the top-left
                .startLocation(0, 0)

                // enable auto animate bubble to the left/right side when release, true by default
                .enableAnimateToEdge(true)

                // set close-bubble icon attributes, currently only drawable and bitmap are supported
                .closeBubble(R.drawable.ic_close_bubble, 60, 60)

                // set style for close-bubble, null by default
                .closeBubbleStyle(null)

                // show close-bubble, true by default
                .enableCloseBubble(true)

                // the more value (dp), the larger closeable-area
                .closablePerimeter(100)

                // choose behavior of the bubbles
                // DYNAMIC_CLOSE_BUBBLE: close-bubble moving based on the bubble's location
                // FIXED_CLOSE_BUBBLE: bubble will automatically move to the close-bubble when it reaches the closable-area
                .behavior(BubbleBehavior.DYNAMIC_CLOSE_BUBBLE)

                // enable bottom background, false by default
                .bottomBackground(false)

                // add listener for the bubble
                .addFloatingBubbleListener(new FloatingBubble.Listener() {
                    @Override
                    public void onDestroy() {}

                    @Override
                    public void onClick() {
                        action.navigateToExpandableView(); // must override `setupExpandableView`, otherwise throw an exception
                    }

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

<details open><summary><b>Kotlin version</b></summary>

```kotlin
class MyService : FloatingBubbleService() {

    /**
     * Sets up a notification for Bubble on Android 8 and up.
     * @param channelId The ID of the notification channel.
     * @return The notification instance.
     */
    override fun setupNotificationBuilder(channelId: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
            .setContentTitle("bubble is running")
            .setContentText("click to do nothing")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }


    override fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder {
        return FloatingBubble.Builder(this)

            // set bubble icon attributes, currently only drawable and bitmap are supported
            .bubble(R.drawable.ic_rounded_blue_diamond, 60, 60)

            // set style for bubble, fade animation by default
            .bubbleStyle(null)

            // set start location of bubble, (x=0, y=0) is the top-left
            .startLocation(0, 0)

            // enable auto animate bubble to the left/right side when release, true by default
            .enableAnimateToEdge(true)

            // set close-bubble icon attributes, currently only drawable and bitmap are supported
            .closeBubble(R.drawable.ic_close_bubble, 60, 60)

            // set style for close-bubble, null by default
            .closeBubbleStyle(null)

            // show close-bubble, true by default
            .enableCloseBubble(true)

            // the more value (dp), the larger closeable-area
            .closablePerimeter(100)

            // choose behavior of the bubbles
            // DYNAMIC_CLOSE_BUBBLE: close-bubble moving based on the bubble's location
            // FIXED_CLOSE_BUBBLE: bubble will automatically move to the close-bubble when it reaches the closable-area
            .behavior(BubbleBehavior.DYNAMIC_CLOSE_BUBBLE)

            // enable bottom background, false by default
            .bottomBackground(false)

            // add listener for the bubble
            .addFloatingBubbleListener(object : FloatingBubble.Listener {
                override fun onDestroy() {}
                override fun onClick() {
                    action.navigateToExpandableView() // must override `setupExpandableView`, otherwise throw an exception
                }
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
            Toast.makeText(this, "hello from card view from kotlin", Toast.LENGTH_SHORT).show();
            action.popToBubble()
        }

        return ExpandableView.Builder(this)

            // You should pass either a view or a compose, not both. Passing both will cause a crash. ❗💥
            // set view to expandable-view
            .view(layout)

            // You should pass either a view or a compose, not both. Passing both will cause a crash. ❗💥
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

### Check if bubble is running:

```java
    Boolean b = FloatingBubbleService.isRunning();
```

### Show expandable view first:

```java
// By default, the service will call showBubble() to display floating bubble on the screen and return START_STICKY.
// If you don't want to show the bubble, you can override onStartCommand like the code below
@Override
public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

    // Assume that you are passing a string with key = "key" to this service.
    String data = intent.getStringExtra("key");

    if (data != null) {
        showExpandableView();
        return START_STICKY;
    }

    showBubble(); // or just let the default behavior take effect: "return super.onStartCommand(intent, flags, startId);"
    return START_STICKY;
}
```

### Detect back-press on expandable-view
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

</br>

<!-- ## III, Sample class 📚  <a name="sample"/> -->



</br>

## IV, Migrate 🚄 <a name="migrate">

If you are using a version prior to 0.5.0, please refer to the change documentation for instructions: [v0.5.0](https://github.com/TorryDo/Floating-Bubble-View/releases/tag/0.5.0)

If you are using an older version then 0.4.0, please follow these guides to migrate: [v0.4.0](https://github.com/TorryDo/Floating-Bubble-View/releases/tag/0.4.0)

## V, Note ✒ <a name="note">
This library is still under heavy development. There is still a lot of code cleanup to do, so expect breaking API changes over time.

Everything's gonna be ok! 🍀 ^^ 

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
