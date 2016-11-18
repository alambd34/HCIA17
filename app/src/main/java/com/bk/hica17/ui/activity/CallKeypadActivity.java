package com.bk.hica17.ui.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bk.hica17.R;
import com.bk.hica17.adapter.ViewPagerAdapter;
import com.bk.hica17.model.DataVoice;
import com.bk.hica17.utils.Util;

public class CallKeypadActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_keypad);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), CallKeypadActivity.this);
        viewPager.setAdapter(viewPagerAdapter);

        final TabLayout.Tab home = tabLayout.newTab();
        final TabLayout.Tab inbox = tabLayout.newTab();
        final TabLayout.Tab star = tabLayout.newTab();
        final TabLayout.Tab upload = tabLayout.newTab();

        home.setIcon(R.drawable.icon_home);
        home.setText("Home");
        setColorFilter(home, getResources().getString(R.string.tab_icon_selected));
        inbox.setIcon(R.drawable.icon_event);
        inbox.setText("Inbox");
        setColorFilter(inbox, getResources().getString(R.string.tab_icon_unselected));
        star.setIcon(R.drawable.icon_personal);
        star.setText("Star");
        setColorFilter(star, getResources().getString(R.string.tab_icon_unselected));
        upload.setIcon(R.drawable.icon_upload);
        upload.setText("Upload");
        setColorFilter(upload, getResources().getString(R.string.tab_icon_unselected));

        tabLayout.addTab(home, 0);
        tabLayout.addTab(inbox, 1);
        tabLayout.addTab(star, 2);
        tabLayout.addTab(upload, 3);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //changeTab(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                setColorFilter(tab, getResources().getString(R.string.tab_icon_selected));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setColorFilter(tab, getResources().getString(R.string.tab_icon_unselected));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Util.checkRecognizerPermission(this);
        DataVoice.initDataVoice();
    }

    public void setColorFilter(TabLayout.Tab tab, String hex) {
        tab.getIcon().setColorFilter(Color.parseColor(hex), PorterDuff.Mode.SRC_IN);
    }


}