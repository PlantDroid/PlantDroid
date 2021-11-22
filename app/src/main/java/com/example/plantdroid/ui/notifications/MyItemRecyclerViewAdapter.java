package com.example.plantdroid.ui.notifications;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.plantdroid.DetailPageActivity;
import com.example.plantdroid.databinding.FragmentItemBinding;

import java.util.ArrayList;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> plant_name;
    private ArrayList<String> plant_picture_url;

    public MyItemRecyclerViewAdapter(ArrayList<String> name, ArrayList<String> picture_url, Context context) {
        plant_name = name;
        plant_picture_url = picture_url;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int plantposition=position;
        holder.mIdView.setText(plant_name.get(position));
        Glide.with(holder.mContentView)
                .load(plant_picture_url.get(position))
                .into(holder.mContentView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.mCardView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, DetailPageActivity.class);
                            intent.putExtra("plantName", plant_name.get(plantposition));
                            mContext.startActivity(intent);
                        }
                    }
            );
        }

    }


    @Override
    public int getItemCount() {
        if (plant_name != null) {
            return plant_name.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final ImageView mContentView;
        public final CardView mCardView;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mCardView = binding.plantDetail;
        }

        @Override

        public String toString() {
            return super.toString() + " '" + mContentView.toString() + "'";
        }
    }
}