# Floating-Bubble-View-Library-Android

I, Prepare
- download source code and integrate module into your app directly

II, How to use

- Firstly, create a class extending FloatingBubbleService class
- Secondly, override 2 function 'setupBubble' and 'setupExpandableView'
- Finally, setup your own builder and enjoy ^^

```java
public class MyService extends FloatingBubbleService {
    
    @NonNull
    @Override
    public FloatingBubbleBuilder setupBubble() {
        return new FloatingBubbleBuilder()
                
                // this is required
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
                .setAlpha(1f);
    }


    @NonNull
    @Override
    public ExpandableViewBuilder setupExpandableView(@NonNull ExpandableViewListener listener) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_view_test, null);

        CardView card = layout.findViewById(R.id.card_view);

        card.setOnClickListener(v -> {
            Toast.makeText(this, "hello from card view from java", Toast.LENGTH_SHORT).show();
            listener.popToBubble();
        });

        return new ExpandableViewBuilder()
                .with(this)
                .setExpandableView(layout);
    }
}
```