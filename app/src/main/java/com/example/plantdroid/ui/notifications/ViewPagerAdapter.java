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
    public Fragment getItem(int position) {
        if (position==0){
            return (MainFragment)mFragments.get(position);
        }else if (position==1){
            return (MainFragment2) mFragments.get(position);
        }else if (position==2){
            return (MainFragment3) mFragments.get(position);
        }else if (position==3){
            return (MainFragment4) mFragments.get(position);
        }else if (position==4){
            return (MainFragment5) mFragments.get(position);
        }else if (position==5){
            return (MainFragment6) mFragments.get(position);
        }else if (position==6){
            return (MainFragment7) mFragments.get(position);
        }
        return (MainFragment8) mFragments.get(position);
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