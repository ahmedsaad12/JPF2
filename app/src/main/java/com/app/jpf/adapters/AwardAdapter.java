package com.app.jpf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_award.AwardActivity;
import com.app.jpf.databinding.AwardRowBinding;
import com.app.jpf.databinding.PointsRowBinding;
import com.app.jpf.models.MyPointsModel;

import java.util.List;

public class AwardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyPointsModel.Prize> list;
    private Context context;
    private LayoutInflater inflater;
    private AwardActivity activity;
    public AwardAdapter(List<MyPointsModel.Prize> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AwardActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AwardRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.award_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            activity.openSheet(list.get(myHolder.getAdapterPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private AwardRowBinding binding;

        public MyHolder(AwardRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
