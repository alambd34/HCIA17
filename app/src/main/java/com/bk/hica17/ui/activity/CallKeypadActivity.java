package com.bk.hica17.ui.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.bk.hica17.R;
import com.bk.hica17.adapter.ViewPagerAdapter;
import com.bk.hica17.model.DataVoice;
import com.bk.hica17.ui.fragment.ContactFragment;
import com.bk.hica17.utils.Util;

public class CallKeypadActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private boolean statusSearchContact = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_keypad);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), CallKeypadActivity.this);
        viewPager.setAdapter(viewPagerAdapter);

        final TabLayout.Tab tabKeyboard = tabLayout.newTab();
        final TabLayout.Tab tabDiary = tabLayout.newTab();
        final TabLayout.Tab tabContact = tabLayout.newTab();
        final TabLayout.Tab tabAdd = tabLayout.newTab();

        tabKeyboard.setIcon(R.drawable.ic_tab_home);
        tabKeyboard.setText(getResources().getString(R.string.tab_keyboard));
        setColorFilter(tabKeyboard, getResources().getString(R.string.bg_tab_selected));
        tabDiary.setIcon(R.drawable.ic_tab_diary);
        tabDiary.setText(getResources().getString(R.string.tab_diary));
        setColorFilter(tabDiary, getResources().getString(R.string.bg_tab_unselected));
        tabContact.setIcon(R.drawable.ic_tab_contact);
        tabContact.setText(getResources().getString(R.string.tab_contact));
        setColorFilter(tabContact, getResources().getString(R.string.bg_tab_unselected));
        tabAdd.setIcon(R.drawable.ic_tab_final);
        tabAdd.setText(getResources().getString(R.string.tab_add));
        setColorFilter(tabAdd, getResources().getString(R.string.bg_tab_unselected));

        tabLayout.addTab(tabKeyboard, 0);
        tabLayout.addTab(tabDiary, 1);
        tabLayout.addTab(tabContact, 2);
        tabLayout.addTab(tabAdd, 3);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.bg_tab_selected));
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
                setColorFilter(tab, getResources().getString(R.string.bg_tab_selected));
                if (tab.getPosition() == 2 && statusSearchContact) {
                    EditText editSearch = ((ContactFragment) viewPagerAdapter.getItem(tab.getPosition())).mEdSearch;
                    Util.setHideSoftInput(editSearch, false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setColorFilter(tab, getResources().getString(R.string.bg_tab_unselected));
                int pos = tab.getPosition();
                if (pos == 2) {
                    EditText editSearch = ((ContactFragment) viewPagerAdapter.getItem(tab.getPosition())).mEdSearch;
                    Util.setHideSoftInput(editSearch, true);
                }
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