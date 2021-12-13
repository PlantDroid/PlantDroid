package com.example.plantdroid.ui.notifications;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.plantdroid.R;
import com.example.plantdroid.Search_result;

import com.example.plantdroid.databinding.FragmentNotificationsBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "Notificationpage";
    private TabLayout mTabLayout;
    private EditText Search_context;
    private ViewPager mViewPager;
    private ImageButton Search_button;
    //左右滑动的标题
    private String[] mTitles = {"All","Pinopsida", "Cycadopsida", "Polypodiopsida", "Magnoliopsida", "Streptophyta", "Basidiomycota", "Other"};
    //每个标题对应的Fragment
    private List<Fragment> fragments;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mTabLayout = root.findViewById(R.id.tablayout);
        mViewPager = root.findViewById(R.id.viewpager);
        Search_context = root.findViewById(R.id.search);
        Search_button = root.findViewById(R.id.search_button);
        Search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Search_context.getText().toString().length() > 0) {
                    Intent intent = new Intent(getActivity(), Search_result.class);
                    String message = Search_context.getText().toString();
                    intent.putExtra("Search_context", message);
                    startActivity(intent);
                } else {
                    //弹出弹窗提示没有输入内容
                    CharSequence text = "No search was entered!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }
            }
        });
        Search_context.addTextChangedListener(new TextWatcher() {
            //监听是否有输入值，如果有就弹出搜索button
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fragments = new ArrayList<>();

        Fragment fragment1 = MainFragment.newInstance(mTitles[0]);
        fragments.add(fragment1);
        Fragment fragment2 = MainFragment2.newInstance(mTitles[1]);
        fragments.add(fragment2);
        Fragment fragment3 = MainFragment3.newInstance(mTitles[2]);
        fragments.add(fragment3);
        Fragment fragment4 = MainFragment4.newInstance(mTitles[3]);
        fragments.add(fragment4);
        Fragment fragment5 = MainFragment5.newInstance(mTitles[4]);
        fragments.add(fragment5);
        Fragment fragment6 = MainFragment6.newInstance(mTitles[5]);
        fragments.add(fragment6);
        Fragment fragment7 = MainFragment7.newInstance(mTitles[6]);
        fragments.add(fragment7);
        Fragment fragment8 = MainFragment8.newInstance(mTitles[7]);
        fragments.add(fragment8);

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