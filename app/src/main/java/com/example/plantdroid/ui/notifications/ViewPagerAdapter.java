package com.example.plantdroid.ui.notifications;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

class ViewPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> mFragments;
    String[] mTitles;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
//    public MainFragment getItem(int position) {
//        return (MainFragment) mFragments.get(position);
//    }
//    public Fragment getItem(int position) {
//        return mFragments.get(position);
//    }
        public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new BlankFragment();
            case 1:
                return new BlankFragment();
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}