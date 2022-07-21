# Floating-Bubble-View-Library-Android

[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
![GitHub stars](https://img.shields.io/github/stars/TorryDo/Floating-Bubble-View?style=social)
![GitHub forks](https://img.shields.io/github/forks/TorryDo/Floating-Bubble-View?label=Fork&style=social)
![Repo size](https://img.shields.io/github/repo-size/TorryDo/Floating-Bubble-View?style=social)

# Demo

https://user-images.githubusercontent.com/85553681/180191018-dd9de96a-ccb5-412a-af33-5d2d50914d8a.mp4

<br/>

# I, Prepare

- ### <b> STEP 1. Adding JitPack repository to your setting.gradle file -------------------------------</b>

Adding maven repository inside "dependencyResolutionManagement => repositories" like below

```diff
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()

            // add here
+           maven { url 'https://jitpack.io' }

            jcenter() // Warning: this repository is going to shut down soon
        }
    }
```

<br/>

### <b>In older gradle version, please follow this step instead</b>

<details><summary><b>Older gradle version</b></summary>

1. Go to your build.gradle (top-level)
2. Add jitpack repo like below

```diff
    // not buildScript
    allprojects {
        repositories {
            ...
+           maven { url 'https://jitpack.io' }
        }
    }

```

</details>

<br/>

<br/>

- ### <b> STEP 2. Add dependency in your build.gradle (app module) -------------------------------</b>

```gradle
    dependencies {
            implementation 'com.github.TorryDo:Floating-Bubble-View:0.3.1'
    }

```

# II, How to use

- ### <b> Step 1 : create a subclass of FloatingBubbleService </b>

```java
    public class MyService extends FloatingBubbleService {
        ...
    }
```

</br>

- ### <b> Step 2 : override 2 methods "setupBubble" and "setupExpandableView" </b>

<details><summary><b>KOTLIN version (click)</b></summary>

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

### > <b> Java version <b>

```java
    public class MyService extends FloatingBubbleService {

        @NonNull
        @Override
        public FloatingBubble.Builder setupBubble() {
            return ...;
        }

        @NonNull
        @Override
        public ExpandableView.Builder setupExpandableView(@NonNull ExpandableView.Action action) {
            return ...;
        }
    }
```

</br>

- ### <b> Step 3 : add your service class in manifest file... </b>

```xml
    <service android:name="<YOUR_PACKAGE>.MyService" />
```

</br>

- ### <b> Step 4 : start your service and enjoy :) </b>

<details><summary><b>KOTLIN version (click)</b></summary>

```kotlin
    val intent = Intent(this, Myservice::class.java)  // 'this' is your activity class

    startService(intent)           // for android version lower than 8 (android O)
    startForegroundService(intent) // for android 8 and higher
```

</details>

### > <b> Java version <b>

```java
    Intent intent = new Intent(MainActivity.this, MyService.class);

    startService(intent);           // for android version lower than 8 (android O)
    startForegroundService(intent); // for android 8 and higher


```

# Sample class
<details><summary><b>KOTLIN version (click)</b></summary>

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

### > <b> Java version <b>

```java
public class MyService extends FloatingBubbleService {

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
