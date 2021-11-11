package com.example.plantdroid.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.plantdroid.R;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {
    private static final String TAG = "!!!";
    TextView mTextView;

    public static MainFragment newInstance(String title) {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        Log.e(TAG, "onCreateView: " +title);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        mTextView = view.findViewById(R.id.textView);
        mTextView.setText((CharSequence) getArguments().get("title"));
        return view;
    }
}