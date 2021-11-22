package com.example.plantdroid.ui.notifications;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.plantdroid.R;
import com.example.plantdroid.databinding.FragmentNotificationsBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "Notificationpage";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    //左右滑动的标题
    private String[] mTitles = {"Angiospermae", "Gymnospermae", "Pteridophyta", "Bryophyta", "Lichens", "Eumycophyta", "Chlorophyta","Other"};
    //每个标题对应的Fragment
    private  List<Fragment> fragments;
    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mTabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        mViewPager = (ViewPager) root.findViewById(R.id.viewpager);
        fragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Fragment fragment=MainFragment.newInstance(mTitles[i]);
            fragments.add(fragment);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), fragments, mTitles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}