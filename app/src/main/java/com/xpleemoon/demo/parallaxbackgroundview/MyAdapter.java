package com.xpleemoon.demo.parallaxbackgroundview;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends PagerAdapter {
    public static final int COUNT = 5;

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView page = (TextView) LayoutInflater.from(container.getContext()).inflate(R.layout.item_page, container, false);
        page.setText(String.valueOf(position + 1));
        container.addView(page);
        return page;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
