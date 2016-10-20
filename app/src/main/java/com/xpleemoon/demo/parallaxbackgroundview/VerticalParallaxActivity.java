package com.xpleemoon.demo.parallaxbackgroundview;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xpleemoon.view.ParallaxBackgroundView;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class VerticalParallaxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_parallax);
        final ParallaxBackgroundView bg = (ParallaxBackgroundView) findViewById(R.id.parallax_bg);
        bg.setParallaxBackgroundResource(R.drawable.vertical_parallax_bg);

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), R.drawable.vertical_parallax_bg, options);
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = 2;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vertical_parallax_bg, options);
//        //noinspection deprecation
//        bg.setParallaxBackground(new BitmapDrawable(bitmap));

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
    }
}
