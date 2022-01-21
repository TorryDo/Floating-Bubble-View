# Floating-Bubble-View-Library-Android

[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
![GitHub stars](https://img.shields.io/github/stars/TorryDo/Floating-Bubble-View?style=social)
![GitHub forks](https://img.shields.io/github/forks/TorryDo/Floating-Bubble-View?label=Fork&style=social)
![Repo size](https://img.shields.io/github/repo-size/TorryDo/Floating-Bubble-View?style=social)

# I, Prepare

<br/>

- STEP 1. Add the JitPack repository to your build.gradle (Project) file

Add it in your root build.gradle at the end of repositories:

```gradle
    // not buildScript
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

```

## If anything go WRONG, forget the step above, then go to your setting.gradle file

then add maven repository inside "dependencyResolutionManagement => repositories"

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // add here
        maven { url 'https://jitpack.io' }

        jcenter() // Warning: this repository is going to shut down soon
    }
}
```

<br/> <br/>

- STEP 2. Add dependency in your app module

```
	dependencies {
	        implementation 'com.github.TorryDo:Floating-Bubble-View:0.1.6'
	}

```

# II, How to use

- Step 1 : create a class extending FloatingBubbleService

```java
public class MyService extends FloatingBubbleService {
    ...
}
```

- Step 2 : override 2 methods "setupBubble" and "setupExpandableView"

```java
public class MyService extends FloatingBubbleService {

    @NonNull
    @Override
    public FloatingBubbleBuilder setupBubble() {
        return ...;
    }

    @NonNull
    @Override
    public ExpandableViewBuilder setupExpandableView(@NonNull ExpandableViewEvent expandableViewEvent) {
        return ...;
    }
}
```

- Step 3 : add your service class (your own class, not "MyService") into the manifest file. :)

```xml
<service android:name="<YOUR_PACKAGE>.MyService" />
```

- Step 4 : start your service and enjoy :)

```java
    Intent intent = new Intent(MainActivity.this, MyService.class);
    startService(intent);
```

# Sample class

```java
public class MyService extends FloatingBubbleService {

    @NonNull
    @Override
    public FloatingBubbleBuilder setupBubble() {
        return new FloatingBubbleBuilder()

                // context is required
                .with(this)

                // set bubble icon
                .setIcon(R.drawable.ic_star)

                // set remove bubble icon
                .setRemoveIcon(R.mipmap.ic_launcher_round)

                // set bubble size in dp
                .setBubbleSizeDp(60)

                // set the point where the bubble appear
                .setStartPoint(-200, 0)

                // set alpha\opacity of the bubble
                .setAlpha(1f)

                // add listener
                .addFloatingBubbleTouchListener(new FloatingBubbleTouchListener() {
                    @Override
                    public void onDestroy() { System.out.println("on Destroy"); }

                    @Override
                    public void onClick() { System.out.println("onClick"); }

                    @Override
                    public void onMove(int x, int y) { System.out.println("onMove"); }

                    @Override
                    public void onUp(int x, int y) { System.out.println("onUp"); }

                    @Override
                    public void onDown(int x, int y) { System.out.println("onDown"); }
                });
    }


    @NonNull
    @Override
    public ExpandableViewBuilder setupExpandableView(@NonNull ExpandableViewEvent expandableViewEvent) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_view_test, null);


        layout.findViewById(R.id.card_view).setOnClickListener(v -> {
            Toast.makeText(this, "hello from card view from java", Toast.LENGTH_SHORT).show();
            expandableViewEvent.popToBubble();
        });

        return new ExpandableViewBuilder()
                .with(this)
                .setExpandableView(layout);
    }
}
```
