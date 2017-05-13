package com.example.trungdinh.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by TrungDinh on 4/11/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mfragments;

    public ViewPagerAdapter(FragmentManager fm , List<Fragment> fragments) {
        super(fm);
        this.mfragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        return mfragments.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

}
