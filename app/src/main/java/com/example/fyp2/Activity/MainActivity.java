package com.example.fyp2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.Fragment.ForecastFragment;
import com.example.fyp2.Fragment.HomeFragment;
import com.example.fyp2.MenuListFragment;
import com.example.fyp2.Fragment.MessageFragment;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;

    private FlowingDrawer mDrawer;
    private NavigationTabStrip mCenterNavigationTabStrip;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UID;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();

        //Re-enabled
        UID = fAuth.getCurrentUser().getUid();
//
//        UID = "Testing ID";

        SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "UID", UID);

        initUI();
        setUI();

    }


    private void initUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
                mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
            }
        });

        setupToolbar();
        setupMenu();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewPager = (ViewPager) findViewById(R.id.vp);

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {

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
            }
        }).start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCenterNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_center);

            }
        });

    }

    private void setUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
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


                        mCenterNavigationTabStrip.setViewPager(mViewPager, position);
//                mViewPager.setCurrentItem(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                mCenterNavigationTabStrip.setViewPager(mViewPager, 0);


                mCenterNavigationTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }).start();


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

    protected void setupToolbar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbar = (Toolbar) findViewById(R.id.toolbar);


                toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDrawer.toggleMenu();
                    }
                });
            }
        }).start();


    }

    private void setupMenu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm = getSupportFragmentManager();
                MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
                if (mMenuFragment == null) {
                    mMenuFragment = new MenuListFragment();

                    fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
                }
            }
        }).start();


//        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
//            @Override
//            public void onDrawerStateChange(int oldState, int newState) {
//                if (newState == ElasticDrawer.STATE_CLOSED) {
//                    Log.i("MainActivity", "Drawer STATE_CLOSED");
//                }
//            }
//
//            @Override
//            public void onDrawerSlide(float openRatio, int offsetPixels) {
//                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}