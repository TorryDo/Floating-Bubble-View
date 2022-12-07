# Floating-Bubble-View-Library-Android



https://user-images.githubusercontent.com/85553681/180191018-dd9de96a-ccb5-412a-af33-5d2d50914d8a.mp4

<br/>

[<img src="https://img.shields.io/badge/platform-Android-yellow.svg" valign="middle">](https://www.android.com)

|  API  | Version | License |
| :---: | :-----: | :-----: | 
| [<img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" valign="middle">](https://android-arsenal.com/api?level=21) | [<img src="https://img.shields.io/maven-central/v/io.github.torrydo/floating-bubble-view" valign="middle">]() | [<img src="https://img.shields.io/badge/License-Apache_2.0-blue.svg" valign="middle">](https://www.apache.org/licenses/LICENSE-2.0) |

</br>

## I, Getting started 🍕🍔🍟
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

## II, Setup & Useage 🚀✈🛰


###  Step 1 : extends FloatingBubbleService then implements `setupBubble()` and `setupExpandableView()` 1️⃣

<details><summary><b>Kotlin version</b></summary>

```kotlin
    class MyService: FloatingBubbleService() {

        override fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder {
            return ...
        }

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

        @Nullable
        @Override
        public ExpandableView.Builder setupExpandableView(@NonNull ExpandableView.Action action) {
            return ...;
        }
    }
```
</details>



</br>

### Step 2 : add your bubble service in your manifest file 2️⃣

```xml
    <application>
        ...
        <service android:name="<YOUR_PACKAGE>.MyService" />

    </application>
```

</br>

### Step 3 : start your service and enjoy 3️⃣ :)

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

> ## API

### Check if bubble is running:

```java
    Boolean b = FloatingBubbleService.isRunning(); // works on both kotlin and java
```

</br>

## Sample class ✌😉

<details><summary><b>Kotlin version</b></summary>

```kotlin
class MyServiceKt : FloatingBubbleService() {

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
        return FloatingBubble.Builder()
            .with(this)
            .setIcon(R.drawable.ic_rounded_blue_diamond)
            .setRemoveIcon(R.drawable.ic_remove_icon)
            .addFloatingBubbleTouchListener(object : FloatingBubble.TouchEvent {
                override fun onDestroy() {
                    println("on Destroy")
                }

                override fun onClick() {
                    action.navigateToExpandableView();
                }

                override fun onMove(x: Int, y: Int) {
                    println("onMove")
                }

                override fun onUp(x: Int, y: Int) {
                    println("onUp")
                }

                override fun onDown(x: Int, y: Int) {
                    println("onDown")
                }
            })
            .setBubbleSizeDp(60)
            .setStartPoint(-200, 0)
            .setAlpha(1f)
    }

    override fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layout = inflater.inflate(R.layout.layout_view_test, null)

        layout.findViewById<View>(R.id.card_view).setOnClickListener { view ->
            Toast.makeText(this, "hello from card view from java", Toast.LENGTH_SHORT).show();
            action.popToBubble()
        }
        return ExpandableView.Builder()
            .with(this)
            .setExpandableView(layout)
            .setDimAmount(0.8f)
    }


}
```

</details>

<details open><summary><b>Java version</b></summary>

```java
public class MyService extends FloatingBubbleService {

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

        return new FloatingBubble.Builder()
                .with(this)
                .setIcon(R.drawable.ic_rounded_blue_diamond)
                .setRemoveIcon(R.drawable.ic_remove_icon)
                .addFloatingBubbleTouchListener(new FloatingBubble.TouchEvent() {
                    @Override
                    public void onDestroy() {
                        System.out.println("on Destroy");
                    }

                    @Override
                    public void onClick() {
                        action.navigateToExpandableView();
                    }

                    @Override
                    public void onMove(int x, int y) {
                        System.out.println("onMove");
                    }

                    @Override
                    public void onUp(int x, int y) {
                        System.out.println("onUp");
                    }

                    @Override
                    public void onDown(int x, int y) {
                        System.out.println("onDown");
                    }
                })
                .setBubbleSizeDp(60)
                .setStartPoint(-200, 0)
                .setAlpha(1f);
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


        return new ExpandableView.Builder()
                .with(this)
                .setExpandableView(layout)
                .setDimAmount(0.8f);
    }
}
```
</details>

</br>

## License

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
