package com.example.second_generation_house.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class Adapter extends FragmentPagerAdapter {
    private Fragment[] fragmentPages;

    public Adapter(FragmentManager fm, Fragment[] fragmentPages) {
        super(fm);
        this.fragmentPages = fragmentPages;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentPages[position];
    }

    @Override
    public int getCount() {
        return fragmentPages.length;
    }
}
