package com.bk.hica17.adapter;

/**
 * Created by khanh on 12/11/2016.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bk.hica17.ui.fragment.CallLogFragment;
import com.bk.hica17.ui.fragment.ContactFragment;
import com.bk.hica17.ui.fragment.HomeFragment;
import com.bk.hica17.ui.fragment.TabFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;


    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
//        return new TabFragment();
        // Which Fragment should be dislpayed by the viewpager for the given position
        // In my case we are showing up only one fragment in all the three tabs so we are
        // not worrying about the position and just returning the TabFragment
        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                return new CallLogFragment();
            case 2:
                return new ContactFragment();
            case 3:
                return new TabFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;           // As there are only 3 Tabs
    }

}

