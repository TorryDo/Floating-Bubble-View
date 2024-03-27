# 🍀Floating Bubble View
An Android library that creates floating bubbles on top of the screen 🎨, supports both XML and 💘 Jetpack Compose

<br>

<div align="center">

<h6> Like this project? 🥰 Don't forget to show some love by giving a Star⭐ </h6>

| Bubble |  Custom | 
| :-: | :-: |
| <img src="https://github.com/TorryDo/assets/blob/main/floating_bubble_view/demo/bubble.gif" height="600" width="272"> | <img src="https://github.com/TorryDo/assets/blob/main/floating_bubble_view/demo/custom_view.gif" height="600" width="276"> |


[<img src="https://img.shields.io/badge/platform-Android-yellow.svg" valign="middle">](https://www.android.com)
[<img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" valign="middle">](https://android-arsenal.com/api?level=21)
[<img src="https://img.shields.io/maven-central/v/io.github.torrydo/floating-bubble-view" valign="middle">]()
[<img src="https://img.shields.io/badge/License-Apache_2.0-blue.svg" valign="middle">](https://www.apache.org/licenses/LICENSE-2.0)

</div>

&nbsp;

## Variants
- ### Flutter
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

2. Ensure the `mavenCentral()` repository is declared in the project-level `build.gradle`/`setting.gradle` file:

    <details><summary>settings.gradle</summary>

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

    <details><summary>build.gradle (project-level) (on old gradle versions)</summary>

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




</details>

<!-- <br> -->

<br>


Declare the dependencies in the module-level `build.gradle` file 🍀 <img src="https://img.shields.io/maven-central/v/io.github.torrydo/floating-bubble-view" valign="middle">

```gradle
dependencies {
    implementation("io.github.torrydo:floating-bubble-view:<LATEST_VERSION>")
}
```
<br>

## II, Setup 🚀✈🛰 <a name="setup_usage"/>

### 1, extends `ExpandableBubbleService()` and call `expand()` or `minimize()` 1️⃣ <a name="setup"/>

<details><summary><b>Java</b></summary>

```java
Java docs is not completed yet because the author is (really) busy and (a little) tired 😪
```
</details>

<details open><summary><b>Kotlin</b></summary>

```kotlin
class MyService: ExpandableBubbleService() {

    override fun onCreate() {
        super.onCreate()
        minimize()
    }

   // optional, only required if you want to call minimize()
   override fun configBubble(): BubbleBuilder? {
       return ...
   }

   // optional, only required if you want to call expand()
   override fun configExpandedBubble(): ExpandedBubbleBuilder? {
       return ...
   }
}
```

</details>

</br>

### 2, add bubble service to the manifest file 2️⃣



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

<details><summary><b>Java</b></summary>

```java
    Intent intent = new Intent(context, MyService.class);
    
    ContextCompat.startForegroundService(this, intent);
    // or
    // startService(intent);           // for android version lower than 8.0 (android O)
    // startForegroundService(intent); // for android 8.0 and higher
```
</details>

<details open><summary><b>Kotlin</b></summary>

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

### 1, `configBubble()` and `configExpandedBubble()`

<details><summary>Java</summary>

```java
public class MyServiceJava extends ExpandableBubbleService {

    @Override
    public void onCreate() {
        super.onCreate();
        minimize();
    }

    @Nullable
    @Override
    public BubbleBuilder configBubble() {
        View imgView = ViewHelper.fromDrawable(this, R.drawable.ic_rounded_blue_diamond, 60, 60);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand();
            }
        });
        return new BubbleBuilder(this)
                .bubbleView(imgView)
                .bubbleStyle(R.style.default_bubble_style)
                .bubbleDraggable(true)
                .forceDragging(true)
                .closeBubbleView(ViewHelper.fromDrawable(this, R.drawable.ic_close_bubble))
                .closeBubbleStyle(R.style.default_close_bubble_style)
                .distanceToClose(100)
                .triggerClickablePerimeterPx(5f)
                .closeBehavior(CloseBubbleBehavior.FIXED_CLOSE_BUBBLE)
                .startLocation(100, 100)
                .enableAnimateToEdge(true)
                .bottomBackground(false)
                .addFloatingBubbleListener(new FloatingBubbleListener() {
                    @Override
                    public void onFingerDown(float x, float y) {}
                    @Override
                    public void onFingerUp(float x, float y) {}
                    @Override
                    public void onFingerMove(float x, float y) {}
                })
                ;
                
    }

    @Nullable
    @Override
    public ExpandedBubbleBuilder configExpandedBubble() {
        View expandedView = LayoutInflater.from(this).inflate(R.layout.layout_view_test, null);

        expandedView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minimize();
            }
        });
        return new ExpandedBubbleBuilder(this)
                .expandedView(expandedView)
                .startLocation(0, 0)
                .draggable(true)
                .style(R.style.default_bubble_style)
                .fillMaxWidth(true)
                .enableAnimateToEdge(true)
                .dimAmount(0.5f);
    }
}
```
</details>

<details open><summary>Kotlin</summary>

```kotlin
class MyServiceKt : ExpandableBubbleService() {

    override fun onCreate() {
        super.onCreate()
        minimize()
    }

    override fun configBubble(): BubbleBuilder? {
        val imgView = ViewHelper.fromDrawable(this, R.drawable.ic_rounded_blue_diamond, 60, 60)
        imgView.setOnClickListener {
            expand()
        }

        return BubbleBuilder(this)
            
            // set bubble view
            .bubbleView(imgView)
            
            // or our sweetie, Jetpack Compose
            .bubbleCompose {
                BubbleCompose()
            }
            
            // set style for the bubble, fade animation by default
            .bubbleStyle(null)
            
            // set start location for the bubble, (x=0, y=0) is the top-left
            .startLocation(100, 100)    // in dp
            .startLocationPx(100, 100)  // in px
            
            // enable auto animate bubble to the left/right side when release, true by default
            .enableAnimateToEdge(true)
            
            // set close-bubble view
            .closeBubbleView(ViewHelper.fromDrawable(this, R.drawable.ic_close_bubble, 60, 60))
            
            // set style for close-bubble, null by default
            .closeBubbleStyle(null)
            
            // DYNAMIC_CLOSE_BUBBLE: close-bubble moving based on the bubble's location
            // FIXED_CLOSE_BUBBLE (default): bubble will automatically move to the close-bubble when it reaches the closable-area
            .closeBehavior(CloseBubbleBehavior.DYNAMIC_CLOSE_BUBBLE)
            
            // the more value (dp), the larger closeable-area
            .distanceToClose(100)
            
            // enable bottom background, false by default
            .bottomBackground(true)
            
            .addFloatingBubbleListener(object : FloatingBubbleListener {
                override fun onFingerMove(x: Float, y: Float) {} // The location of the finger on the screen which triggers the movement of the bubble.
                override fun onFingerUp(x: Float, y: Float) {}   // ..., when finger release from bubble
                override fun onFingerDown(x: Float, y: Float) {} // ..., when finger tap the bubble
            })

            // set the clickable perimeter of the bubble in pixels (default = 5f)
            .triggerClickablePerimeterPx(5f)

    }

    override fun configExpandedBubble(): ExpandedBubbleBuilder? {

        val expandedView = LayoutInflater.from(this).inflate(R.layout.layout_view_test, null)
        expandedView.findViewById<View>(R.id.btn).setOnClickListener {
            minimize()
        }

        return ExpandedBubbleBuilder(this)
            .expandedView(expandedView)
            .expandedCompose { 
                ExpandedCompose()
            }
            // handle key code
            .onDispatchKeyEvent {
                if(it.keyCode == KeyEvent.KEYCODE_BACK){
                    minimize()
                }
                null
            }
            // set start location in dp
            .startLocation(0, 0)
            // allow expanded bubble can be draggable or not
            .draggable(true)
            // fade animation by default
            .style(null)
            // 
            .fillMaxWidth(true)
            // animate to the left/right side when release, trfalseue by default 
            .enableAnimateToEdge(true)
            // set background dimmer
            .dimAmount(0.6f)
    }
}
```

</details>

<br>

### 2, Override default `Notification`
<details><summary>Java</summary>

```java
public class MyService extends ExpandableBubbleService {
    ...
    @Override
    public void startNotificationForeground() {
        startForeground(...);

        // or you can use NotificationHelper class
        // val noti = NotificationHelper(this)
        // noti.createNotificationChannel()
        // startForeground(noti.notificationId, noti.defaultNotification())
    }
}
```

</details>

<details open><summary>Kotlin</summary>

```kotlin
class MyService : FloatingBubbleService() {
    ...
    // optional, of course
    override fun startNotificationForeground() {
        startForeground(...)

        // or you can use NotificationHelper class
        // val noti = NotificationHelper(this)
        // noti.createNotificationChannel()
        // startForeground(noti.notificationId, noti.defaultNotification())
    }
}
```

</details>

<details><summary>Notice since Android 13 ⚠ </summary>
<br/>

Starting in Android 13 (API level 33), notifications are only visible if the "POST_NOTIFICATIONS" permission is granted.<br/>

> The service will run normally even if the notification is not visible. 🍀

> You still need to initialize the notification before showing any view.

</details>

<br>

### 3, Methods in `ExpandableBubbleService`

| Name | Description |
| :- | :- |
| `removeAll()` | remove all bubbles |
| `expand()` | show expanded-bubble |
| `minimize()` | show bubble |
| `enableBubbleDragging()` | enable bubble dragging or not |
| `enableExpandedBubbleDragging()` | enable expanded-bubble dragging or not |
| `animateBubbleToEdge()` | animate bubble to edge of the screen |
| `animateExpandedBubbleToEdge()` | animate expanded-bubble to edge of the screen |

<br>

### 4, Helper Class
- ViewHelper()
     - fromBitmap(context, bitmap): View
     - fromBitmap(context, bitmap, widthDp, heightDp): View
     - fromDrawable(context, drawableRes): View
     - fromDrawable(context, drawableRes, widthDp, heightDp)

- NotificationHelper(context, channelId, channelName, notificationId)
     - notify(Notification): update notification based on notificationId
     - createNotificationChannel(): create notification channel from `android 8` and above
     - defaultNotification(): return default notification

</br>

## IV, Contribution Guide 👏  <a name="contribution_guide">

Contributions are welcome! 🙌

- If you come across a bug or have an idea for a new feature, please let us know by creating an [Issue](https://github.com/TorryDo/Floating-Bubble-View/issues) 🐛💡
- If you're interested in taking on an [open issue](https://github.com/TorryDo/Floating-Bubble-View/issues), please comment on it so others are aware 😊
- If you've already fixed a bug or implemented a feature, feel free to submit a [Pull request](https://github.com/TorryDo/Floating-Bubble-View/pulls) 🚀
- Having questions, ideas, or feedback? Don't worry, I gotchu. Simply open a [Discussion](https://github.com/TorryDo/Floating-Bubble-View/discussions) 🔊
- Find this project useful? 🥰 Don't forget to show some love by giving a star ⭐

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
