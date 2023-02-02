# Floating-Bubble-View-Library-Android


https://user-images.githubusercontent.com/85553681/208131247-37a51e40-6b61-4cf7-b64a-5ad44cb1edb9.mp4


<br/>




| Platform |  API  | Version | License |
| :-: | :-: | :-: | :-: | 
|[<img src="https://img.shields.io/badge/platform-Android-yellow.svg" valign="middle">](https://www.android.com)| [<img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" valign="middle">](https://android-arsenal.com/api?level=21) | [<img src="https://img.shields.io/maven-central/v/io.github.torrydo/floating-bubble-view" valign="middle">]() | [<img src="https://img.shields.io/badge/License-Apache_2.0-blue.svg" valign="middle">](https://www.apache.org/licenses/LICENSE-2.0) |

</br>

## Table of Contents
1. [Getting started](#getting_started)  
2. [Setup and Usage](#setup_usage) 
3. [Sample](#sample)
4. [Note](#note)
5. [License](#license)


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

###  Step 1 : extends `FloatingBubbleService` then implements `setupBubble()`  1️⃣ <a name="setup"/>

<details><summary><b>Kotlin version</b></summary>

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

<details open><summary><b>Java version</b></summary>

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



</br>

### Step 2 : add bubble-service to manifest file 2️⃣

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

### Step 3 : start bubble service and enjoy 3️⃣ :)
> Make sure "display over other apps" permission is granted, otherwise the app will crash
<details><summary><b>Kotlin version</b></summary>

```kotlin
    val intent = Intent(context, Myservice::class.java)

    startService(intent)           // for android version lower than 8 (android O)
    startForegroundService(intent) // for android 8 and higher
```

</details>

<details open><summary><b>Java version</b></summary>


```java
    Intent intent = new Intent(context, MyService.class);

    startService(intent);           // for android version lower than 8 (android O)
    startForegroundService(intent); // for android 8 and higher
```
</details>

</br>

> ## API <a name="api"/>

### Check if bubble is running:

```java
    Boolean b = FloatingBubbleService.isRunning();
```

</br>

## Sample class ✌😉  <a name="sample"/>

<details><summary><b>Kotlin version</b></summary>

```java
class MyServiceKt : FloatingBubbleService() {

    // for android 8 and up
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
            // set style for bubble, by default bubble use fade animation
            .bubbleStyle(null)
            // set start location of bubble, (x=0, y=0) is top-left
            .startLocation(0, 0)
            // enable auto animate bubble to the left/right side when release, true by default
            .enableAnimateToEdge(true)

            // set close-bubble icon attributes, currently only drawable and bitmap are supported
            .closeBubble(R.drawable.ic_remove_icon, 60, 60)
            // set style for close-bubble, null by default
            .closeBubbleStyle(null)
            // show close-bubble, true by default
            .enableCloseBubble(true)
            // enable bottom background, false by default
            .bottomBackground(false)

            .addFloatingBubbleListener(object : FloatingBubble.Listener {
                override fun onDestroy() {}
                override fun onClick() {
                    action.navigateToExpandableView() // must override `setupExpandableView`, otherwise throw an exception
                }
                override fun onMove(x: Int, y: Int) {}
                override fun onUp(x: Int, y: Int) {}
                override fun onDown(x: Int, y: Int) {}
            })
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
            .expandableView(layout)
            .dimAmount(0.8f)
            .addExpandableViewListener(object : ExpandableView.Listener {
                override fun onOpenExpandableView() {}
                override fun onCloseExpandableView() {}
            })
            .expandableViewStyle(null)
    }


}
```

</details>

<details open><summary><b>Java version</b></summary>

```java
public class MyService extends FloatingBubbleService {

    // Optional. If not override. show default notification
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

        return new FloatingBubble.Builder(this)

                // set bubble icon attributes, currently only drawable and bitmap are supported
                .bubble(R.drawable.ic_rounded_blue_diamond, 60, 60)
                // set style for bubble, by default bubble use fade animation
                .bubbleStyle(null)
                // set start location of bubble, (x=0, y=0) is top-left
                .startLocation(0, 0)
                // enable auto animate bubble to the left/right side when release, true by default
                .enableAnimateToEdge(true)

                // set close-bubble icon attributes, currently only drawable and bitmap are supported
                .closeBubble(R.drawable.ic_remove_icon, 60, 60)
                // set style for close-bubble, null by default
                .closeBubbleStyle(null)
                // show close-bubble, true by default
                .enableCloseBubble(true)
                // enable bottom background, false by default
                .bottomBackground(false)

                .addFloatingBubbleListener(new FloatingBubble.Listener() {
                    @Override
                    public void onDestroy() {}

                    @Override
                    public void onClick() {
                        action.navigateToExpandableView(); // must override `setupExpandableView`, otherwise throw an exception
                    }

                    @Override
                    public void onMove(int x, int y) {}

                    @Override
                    public void onUp(int x, int y) {}

                    @Override
                    public void onDown(int x, int y) {}
                })
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
                .expandableView(layout)
                .dimAmount(0.8f)
                .addExpandableViewListener(new ExpandableView.Listener() {
                    @Override
                    public void onOpenExpandableView() {}

                    @Override
                    public void onCloseExpandableView() {}
                })
                .expandableViewStyle(null);
    }
}
```
</details>

</br>

## Note <a name="note">
This library is still under heavy development. There is still a lot of code cleanup to do, so expect breaking API changes over time.

Everything's gonna be ok! ^^

## License <a name="license"/>

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
