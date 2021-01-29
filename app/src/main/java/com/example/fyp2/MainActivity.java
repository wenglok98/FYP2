package com.example.fyp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;

    private NavigationTabStrip mCenterNavigationTabStrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        setUI();

    }
    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mCenterNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_center);

    }
    private void setUI() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ForecastFragment());
        fragmentList.add(new MessageFragment());

        //为Viewpager设置Adapter
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mCenterNavigationTabStrip.setViewPager(mViewPager, 1);


        mCenterNavigationTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        final NavigationTabStrip navigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts);
//        navigationTabStrip.setTitles("Nav", "Tab", "Strip");
//        navigationTabStrip.setTabIndex(0, true);
//        navigationTabStrip.setTitleSize(15);
//        navigationTabStrip.setStripColor(Color.RED);
//        navigationTabStrip.setStripWeight(6);
//        navigationTabStrip.setStripFactor(2);
//        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
//        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
//        navigationTabStrip.setTypeface("fonts/typeface.ttf");
//        navigationTabStrip.setCornersRadius(3);
//        navigationTabStrip.setAnimationDuration(300);
//        navigationTabStrip.setInactiveColor(Color.GRAY);
//        navigationTabStrip.setActiveColor(Color.WHITE);
//        navigationTabStrip.setOnPageChangeListener(...);
//        navigationTabStrip.setOnTabStripSelectedIndexListener(...);
    }

}