package com.xpleemoon.demo.parallaxbackgroundview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startHorizontalParallax(View view) {
        startActivity(new Intent(this, HorizontalParallaxActivity.class));
    }

    public void startVerticalParallax(View view) {
        startActivity(new Intent(this, VerticalParallaxActivity.class));
    }
}
