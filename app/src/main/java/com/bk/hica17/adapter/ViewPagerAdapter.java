package com.bk.hica17.adapter;

/**
 * Created by khanh on 12/11/2016.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.ui.fragment.HomeFragment;
import com.bk.hica17.ui.fragment.TabFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String tabTitles[] = new String[] { "Home", "Inbox", "Star" , "Upload"};
    private int[] imageResId = { R.drawable.icon_home_press, R.drawable.icon_event, R.drawable.icon_personal, R.drawable.icon_upload };
    private Context context;
    private ImageView[] img = new ImageView[4];

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
//                setImageDefault();
//                imageResId[position] = R.drawable.icon_home_press;
//                img[position].setImageResource(imageResId[position]);
                return tab1;
            case 1:
                TabFragment tab2 = new TabFragment();
//                setImageDefault();
//                imageResId[position] = R.drawable.icon_event_press;
//                img[position].setImageResource(imageResId[position]);
                return tab2;
            case 2:
                TabFragment tab3 = new TabFragment();
//                setImageDefault();
//                imageResId[position] = R.drawable.icon_personal_press;
//                img[position].setImageResource(imageResId[position]);
                return tab3;
            case 3:
                TabFragment tab4 = new TabFragment();
//                setImageDefault();
//                imageResId[position] = R.drawable.icon_upload_press;
//                img[position].setImageResource(imageResId[position]);
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;           // As there are only 3 Tabs
    }


    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_view, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        img[position] = (ImageView) v.findViewById(R.id.imageView);
        img[position].setImageResource(imageResId[position]);
        return v;
    }

    private void setImageDefault() {
        imageResId[0] = R.drawable.icon_home;
        imageResId[1] = R.drawable.icon_event;
        imageResId[2] = R.drawable.icon_personal;
        imageResId[3] = R.drawable.icon_upload;
        for (int i = 0; i < 4; ++i) {
            img[i].setImageResource(imageResId[i]);
        }
    }

}

