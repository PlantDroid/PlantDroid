package com.example.plantdroid.ui.notifications;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plantdroid.databinding.FragmentItemBinding;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private String[] plant_name;
    private String[] plant_picture_url;

    public MyItemRecyclerViewAdapter(String[] name,String[] picture_url) {
        plant_name=name;
        plant_picture_url=picture_url;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(plant_name[position]);
        holder.mContentView.setImageDrawable(Drawable.createFromPath(plant_picture_url[position]));
    }

    @Override
    public int getItemCount() {
        return plant_name.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final ImageView mContentView;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView=binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.toString()+ "'";
        }
    }
}