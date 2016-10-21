# ParallaxBackgroundView

A parallax background view that is support horizontal or vertical. Horizontal and vertical parallax are set via

```Java
	setParallaxOrientation(int); // attr ref com.xpleemoon.view.R.styleable#ParallaxBackgroundView_parallaxOrientation)
```

Parallax background is set via

```Java
	// setParallaxBackground(Drawable);
	setParallaxBackgroundResource(int); // attr ref com.xpleemoon.view.R.styleable#ParallaxBackgroundView_parallaxBackground
```

Parallax offset is set via

```
	setParallaxPercent(float)
```

**Notice**, there are 2 modes are used for scaling：

  - MODE_PRE_SCALE: uses more memory but scrolls slightly smoother
  - MODE_POST_SCALE: uses less memory but is more CPU-intensive

**Default attribute**：

  - **isParallax**——true
  - **parallaxMode**——postScale
  - **parallaxOrientation**——horizontal

## Screenshots

![horizontal_parallax_bg](https://github.com/xpleemoon/ParallaxBackgroundView/blob/master/art/horizontal_parallax_bg.gif?raw=true)
![vertical_parallax_bg](https://github.com/xpleemoon/ParallaxBackgroundView/blob/master/art/vertical_parallax_bg.gif?raw=true)

## Download

use gradle:

```
compile 'com.xpleemoon.view:parallaxbackgroundview:1.0.1'
```
or use maven

```
<dependency>
  <groupId>com.xpleemoon.view</groupId>
  <artifactId>parallaxbackgroundview</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```


## How do I use ParallaxBackgroundView?

Parallax effect using 2 layers: One for the content（ ScrollView、ListView or RecyclerView etc） and another one for the background（ParallaxBackgroundView）.

Simple use cases will look like the following two ways: via xml attribute or via java code

### via xml attribute

```Xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.xpleemoon.demo.parallaxbackgroundview.HorizontalParallaxActivity">

    <com.xpleemoon.view.ParallaxBackgroundView
        android:id="@+id/parallax_bg"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:isParallax="true"
        app:parallaxMode="postScale"
        app:parallaxOrientation="horizontal"
        app:parallaxBackground="@drawable/horizontal_parallax_bg"/>

    <android.support.v4.view.ViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</RelativeLayout>
```
```Java
        final ParallaxBackgroundView bg = (ParallaxBackgroundView) findViewById(R.id.parallax_bg);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new MyAdapter();
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // this is called while user's flinging with:
                // position is the page number
                // positionOffset is the percentage scrolled (0...1)
                // positionOffsetPixels is the pixel offset related to that percentage
                // so we got everything we need ....
                float finalPercentage = ((position + positionOffset) * 100 / adapter.getCount()); // percentage of this page+offset respect the total pages
                // now you have to scroll the background layer to this position. You can either adjust the clipping or
                // the background X coordinate, or a scroll position if you use an image inside an scrollview ...
                // I personally like to extend View and draw a scaled bitmap with a clipping region (drawBitmap with Rect parameters), so just modifying the X position then calling invalidate will do. See attached source ParallaxBackground
                bg.setParallaxPercent(finalPercentage);
            }
        });
```

### via java code

```Xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.xpleemoon.demo.parallaxbackgroundview.VerticalParallaxActivity">

    <com.xpleemoon.view.ParallaxBackgroundView
        android:id="@+id/parallax_bg"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <fr.castorflex.android.verticalviewpager.VerticalViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</RelativeLayout>
```
```Java
        final ParallaxBackgroundView bg = (ParallaxBackgroundView) findViewById(R.id.parallax_bg);
        bg.setParallax(true);
        bg.setParallaxMode(ParallaxBackgroundView.MODE_POST_SCALE);
        bg.setParallaxOrientation(ParallaxBackgroundView.PARALLAX_VERTICAL);
        bg.setParallaxBackgroundResource(R.drawable.vertical_parallax_bg);

        VerticalViewPager pager = (VerticalViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new MyAdapter();
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // this is called while user's flinging with:
                // position is the page number
                // positionOffset is the percentage scrolled (0...1)
                // positionOffsetPixels is the pixel offset related to that percentage
                // so we got everything we need ....
                float finalPercentage = ((position + positionOffset) * 100 / adapter.getCount()); // percentage of this page+offset respect the total pages
                // now you have to scroll the background layer to this position. You can either adjust the clipping or
                // the background X coordinate, or a scroll position if you use an image inside an scrollview ...
                // I personally like to extend View and draw a scaled bitmap with a clipping region (drawBitmap with Rect parameters), so just modifying the X position then calling invalidate will do. See attached source ParallaxBackground
                bg.setParallaxPercent(finalPercentage);
            }
        });
```

## Thanks

- [rupps](http://stackoverflow.com/questions/17207612/how-to-have-a-wider-image-scrolling-in-the-background?answertab=votes#tab-top)
- [Antoine Merle](https://github.com/castorflex/VerticalViewPager)
