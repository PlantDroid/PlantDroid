package com.example.plantdroid.ui.notifications;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plantdroid.R;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String[] name = {"肖申克的救赎", "这个杀手不太冷", "霸王别姬", "泰坦尼克号", "瓦力"};
    private String[] picture_url = {"https://tse1-mm.cn.bing.net/th/id/R-C.49705e2909f66cc8a21e8a657819975c?rik=O%2f0XI5s%2f87PnNg&riu=http%3a%2f%2fimage.huahuibk.com%2fuploads%2f20190119%2f16%2f1547885202-IOApmoKPfT.jpeg&ehk=yjJ2CzidOQrrnV6pgiNxo6EKxS9PnJBfkFNgO6pv%2f1I%3d&risl=&pid=ImgRaw&r=0", "https://tse1-mm.cn.bing.net/th/id/R-C.49705e2909f66cc8a21e8a657819975c?rik=O%2f0XI5s%2f87PnNg&riu=http%3a%2f%2fimage.huahuibk.com%2fuploads%2f20190119%2f16%2f1547885202-IOApmoKPfT.jpeg&ehk=yjJ2CzidOQrrnV6pgiNxo6EKxS9PnJBfkFNgO6pv%2f1I%3d&risl=&pid=ImgRaw&r=0", "https://tse1-mm.cn.bing.net/th/id/R-C.49705e2909f66cc8a21e8a657819975c?rik=O%2f0XI5s%2f87PnNg&riu=http%3a%2f%2fimage.huahuibk.com%2fuploads%2f20190119%2f16%2f1547885202-IOApmoKPfT.jpeg&ehk=yjJ2CzidOQrrnV6pgiNxo6EKxS9PnJBfkFNgO6pv%2f1I%3d&risl=&pid=ImgRaw&r=0", "https://tse1-mm.cn.bing.net/th/id/R-C.49705e2909f66cc8a21e8a657819975c?rik=O%2f0XI5s%2f87PnNg&riu=http%3a%2f%2fimage.huahuibk.com%2fuploads%2f20190119%2f16%2f1547885202-IOApmoKPfT.jpeg&ehk=yjJ2CzidOQrrnV6pgiNxo6EKxS9PnJBfkFNgO6pv%2f1I%3d&risl=&pid=ImgRaw&r=0", "https://tse1-mm.cn.bing.net/th/id/R-C.49705e2909f66cc8a21e8a657819975c?rik=O%2f0XI5s%2f87PnNg&riu=http%3a%2f%2fimage.huahuibk.com%2fuploads%2f20190119%2f16%2f1547885202-IOApmoKPfT.jpeg&ehk=yjJ2CzidOQrrnV6pgiNxo6EKxS9PnJBfkFNgO6pv%2f1I%3d&risl=&pid=ImgRaw&r=0"};

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(name, picture_url));
        }
        return view;
    }

}