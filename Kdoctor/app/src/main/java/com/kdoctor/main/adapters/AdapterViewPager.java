package com.kdoctor.main.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by Huy on 11/6/2016.
 */

public class AdapterViewPager extends FragmentStatePagerAdapter {
    List<Fragment> lstFragments;

    public AdapterViewPager(FragmentManager fm, List<Fragment> lstFragments) {
        super(fm);
        this.lstFragments = lstFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return lstFragments.get(position);
    }

    @Override
    public int getCount() {
        return lstFragments.size();
    }
}
