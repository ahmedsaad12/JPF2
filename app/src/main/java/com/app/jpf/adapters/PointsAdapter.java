package com.app.jpf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_points.PointsActivity;
import com.app.jpf.databinding.PointsRowBinding;
import com.app.jpf.databinding.ShopGalleryRowBinding;
import com.app.jpf.models.MyPointsModel;

import java.util.List;

public class PointsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyPointsModel> list;
    private Context context;
    private LayoutInflater inflater;
    private PointsActivity activity;
    public PointsAdapter(List<MyPointsModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (PointsActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PointsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.points_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private PointsRowBinding binding;

        public MyHolder(PointsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
