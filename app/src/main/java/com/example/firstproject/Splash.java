package com.example.firstproject;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.firstproject.common.onBoardingFragment1;
import com.example.firstproject.common.onBoardingFragment2;
import com.example.firstproject.common.onBoardingFragment3;
import com.example.firstproject.common.onBoardingFragment4;

import java.util.Timer;

import pl.droidsonroids.gif.GifImageView;

public class Splash extends AppCompatActivity {

    ImageView img;
    GifImageView gifImageView;
    LinearLayout dotsLayout;
    TextView dots[];
    Timer timer;
    private static final int NUM_PAGES = 4;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        img=findViewById(R.id.bgimg);
        gifImageView=findViewById(R.id.gifImageView);
        dotsLayout=findViewById(R.id.dotsLayout);
        timer = new Timer();
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        img.animate().translationX(-1400).setDuration(1000).setStartDelay(4000);
        gifImageView.animate().translationX(-1400).setDuration(1000).setStartDelay(4000);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);


    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    onBoardingFragment1 tab1 = new onBoardingFragment1();
                    return tab1;
                case 1:
                    onBoardingFragment2 tab2 = new onBoardingFragment2();
                    return tab2;
                case 2:
                    onBoardingFragment3 tab3 = new onBoardingFragment3();
                    return tab3;
                case 3:
                    onBoardingFragment4 tab4 = new onBoardingFragment4();
                    return tab4;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @SuppressLint("ResourceType")
    public void addDots(int position){
        dots = new TextView[4];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setPadding(0,0,10,0);
            dots[i].setTextColor(getResources().getColor(R.color.dots_background));
            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.dots_color));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            if(position == 3){
                findViewById(R.id.skip_btn).setAlpha(0);
                findViewById(R.id.skip_btn).setClickable(false);
            }else {
                findViewById(R.id.skip_btn).setAlpha(1);
                findViewById(R.id.skip_btn).setClickable(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public void callSign_Up_skip(View view){
        Intent intent = new Intent(getApplicationContext(),Start_Up.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View,String>(findViewById(R.id.skip_btn),"transition_startup");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash.this,pairs);
        startActivity(intent,options.toBundle());
    }

    public void callSign_Up(View view){
        Intent intent = new Intent(getApplicationContext(),Start_Up.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View,String>(findViewById(R.id.next_lottie),"transition_startup");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash.this,pairs);
        startActivity(intent,options.toBundle());
    }
}